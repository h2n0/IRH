package com.fls.irh.main.updater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fls.irh.main.Game;

public class GameUpdater {
    /*
     * GameUpdater.Java Checks if the games version is out dated / downloads new
     * updates (.JAR). Author: GabrielBailey74
     */
    private static byte[] BYTE_BUFFER = new byte[1024];
    private String currentClientVersion;
    private String versionLocation;
    private String versionFileNameAndExt;
    private String gameName;
    private String mostRecentVersion;
    private boolean needsUpdating;
    private int downloadPercentage;
    private int kbs;
    private double ETA;

    /**
     * Main entry point into the application
     * 
     * @param args
     *            The arguments passed via the user (NONE)
     * @throws IOException
     *             There was a error during reading / writing.
     * @throws MalformedURLException
     *             URL is missing headers?
     */
    public static void main(String[] args) throws MalformedURLException, IOException {
        /* Parameters for the GameUpdater constructor */
        String currentClientVersion = Game.version;
        String versionDirectoryURL = "http://h2n0.cggame.net76.net/IRH-rl/";
        String versionFileNameAndExt = "version.txt";
        String gameName = "IRH";
        GameUpdater updater = new GameUpdater(currentClientVersion, versionDirectoryURL, versionFileNameAndExt, gameName);
        if (updater.needsUpdating()) {
            updater.downloadUpdatedJar(true); // true to print percentage while
                                              // downloading
        }
    }

    public GameUpdater(String currentClientVersion, String versionLocation,
            String versionFileNameAndExtension, String gameName) throws MalformedURLException,
            IOException {
        setCurrentClientVersion(currentClientVersion);
        setVersionLocation(versionLocation);
        setVersionFileNameAndExt(versionFileNameAndExtension);
        setGameName(gameName);
        String version = getVersionFromUrl(versionLocation + versionFileNameAndExtension);
        setMostRecentVersion(version);
        System.out.println("Current client version: " + currentClientVersion);
        System.out.println("Actual client version: " + version);
        setNeedsUpdating(!currentClientVersion.equals(version));
        System.out.println("Client needs updating: " + needsUpdating());

    }

    private File updateJar(String jarLocation, boolean printPercentage)
            throws MalformedURLException, IOException {
        return getJarFromURL(jarLocation, getGameName() + getMostRecentVersion() + ".jar",
                printPercentage);
    }

    private File downloadUpdatedJar(boolean printPercentage) throws MalformedURLException,
            IOException {
        return updateJar(getVersionLocation() + getGameName() + getMostRecentVersion() + ".jar",
                printPercentage);
    }

    private String getVersionFromUrl(String versionFileURL) throws MalformedURLException,
            IOException {
        /*
         * Initialize the Input Stream. (Downloading from remote system /
         * server)
         */
        BufferedInputStream in = new BufferedInputStream(new URL(versionFileURL).openStream());

        /*
         * Initialize the Output Stream. (Downloading on local system / pc)
         */
        File savedFile = new File("version.txt.tmp");
        savedFile.deleteOnExit();
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(savedFile));

        /*
         * Begin the download.
         */
        int inCount;
        while ((inCount = in.read(BYTE_BUFFER, 0, BYTE_BUFFER.length)) != -1) {
            out.write(BYTE_BUFFER, 0, inCount);
        }

        /*
         * Close the Input/Output streams.
         */
        out.flush();
        out.close();
        in.close();

        // after the version data has been saved, read it and delete the .TMP
        // file.
        String version = "";
        if (savedFile.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(savedFile));
            version = br.readLine();
            br.close();
        }
        return version;
    }

    private File getJarFromURL(String urlLocation, String saveLocation, boolean printPercentage)
            throws MalformedURLException, IOException {
        double numWritten = 0;
        double length = getURLSizeInKB(urlLocation);

        /*
         * Initialize the Input Stream. (Downloading from remote system /
         * server)
         */
        BufferedInputStream in = new BufferedInputStream(new URL(urlLocation).openStream());

        /*
         * Initialize the Output Stream. (Downloading on local system / pc)
         */
        File savedFile = new File(saveLocation);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(savedFile));

        /*
         * Keeping track of when we started.
         */
        long startTime = System.currentTimeMillis();

        /*
         * Begin the download.
         */
        int inCount;
        while ((inCount = in.read(BYTE_BUFFER, 0, BYTE_BUFFER.length)) != -1) {
            out.write(BYTE_BUFFER, 0, inCount);
            numWritten += inCount;

            /*
             * Calculate the Percentage.
             */
            setDownloadPercentage((int) (((double) numWritten / (double) length) * 100D));
            if (printPercentage) {
                System.out.println("Download is " + getDownloadPercentage() + "% complete, "
                        + (int) getKbs() + " seconds left");
            }

            /*
             * Calculate the KBS.
             */
            setKbs((int) ((numWritten / BYTE_BUFFER.length) / (1 + ((System.currentTimeMillis() - startTime) / 1000))));

            /*
             * Calculate the ETA.
             */
            setETA((length - numWritten) / kbs / 1000D);
        }

        /*
         * Close the Input/Output streams.
         */
        out.flush();
        out.close();
        in.close();
        return savedFile;
    }

    /* Obtaining the files size in kilobytes */
    private Double getURLSizeInKB(String urlStr) {
        double contentLength = 0;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            contentLength = httpConn.getContentLength();
            httpConn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return contentLength;
    }

    private boolean needsUpdating() {
        return needsUpdating;
    }

    public String getCurrentClientVersion() {
        return currentClientVersion;
    }

    public void setCurrentClientVersion(String currentClientVersion) {
        this.currentClientVersion = currentClientVersion;
    }

    public String getVersionLocation() {
        return versionLocation;
    }

    public void setVersionLocation(String versionLocation) {
        this.versionLocation = versionLocation;
    }

    public String getVersionFileNameAndExt() {
        return versionFileNameAndExt;
    }

    public void setVersionFileNameAndExt(String versionFileNameAndExt) {
        this.versionFileNameAndExt = versionFileNameAndExt;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setNeedsUpdating(boolean needsUpdating) {
        this.needsUpdating = needsUpdating;
    }

    public String getMostRecentVersion() {
        return mostRecentVersion;
    }

    public void setMostRecentVersion(String mostRecentVersion) {
        this.mostRecentVersion = mostRecentVersion;
    }

    public int getDownloadPercentage() {
        return downloadPercentage;
    }

    public void setDownloadPercentage(int downloadPercentage) {
        this.downloadPercentage = downloadPercentage;
    }

    public int getKbs() {
        return kbs;
    }

    public void setKbs(int kbs) {
        this.kbs = kbs;
    }

    public double getETA() {
        return ETA;
    }

    public void setETA(double eTA) {
        ETA = eTA;
    }

}
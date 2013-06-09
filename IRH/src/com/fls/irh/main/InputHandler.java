package com.fls.irh.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler
  implements KeyListener
{
  public List<Key> keys = new ArrayList<Key>();

  public Key up = new Key();
  public Key down = new Key();
  public Key left = new Key();
  public Key right = new Key();
  public Key attack = new Key();
  public Key menu = new Key();

  public void releaseAll() {
    for (int i = 0; i < this.keys.size(); i++)
      ((Key)this.keys.get(i)).down = false;
  }

  public void tick()
  {
    for (int i = 0; i < this.keys.size(); i++)
      ((Key)this.keys.get(i)).tick();
  }

  public InputHandler(Game game)
  {
    game.addKeyListener(this);
  }

  public void keyPressed(KeyEvent ke) {
    toggle(ke, true);
  }

  public void keyReleased(KeyEvent ke) {
    toggle(ke, false);
  }

  private void toggle(KeyEvent ke, boolean pressed) {
    if (ke.getKeyCode() == 104) this.up.toggle(pressed);
    if (ke.getKeyCode() == 98) this.down.toggle(pressed);
    if (ke.getKeyCode() == 100) this.left.toggle(pressed);
    if (ke.getKeyCode() == 102) this.right.toggle(pressed);
    if (ke.getKeyCode() == 87) this.up.toggle(pressed);
    if (ke.getKeyCode() == 83) this.down.toggle(pressed);
    if (ke.getKeyCode() == 65) this.left.toggle(pressed);
    if (ke.getKeyCode() == 68) this.right.toggle(pressed);
    if (ke.getKeyCode() == 38) this.up.toggle(pressed);
    if (ke.getKeyCode() == 40) this.down.toggle(pressed);
    if (ke.getKeyCode() == 37) this.left.toggle(pressed);
    if (ke.getKeyCode() == 39) this.right.toggle(pressed);

    if (ke.getKeyCode() == 9) this.menu.toggle(pressed);
    if (ke.getKeyCode() == 18) this.menu.toggle(pressed);
    if (ke.getKeyCode() == 65406) this.menu.toggle(pressed);
    if (ke.getKeyCode() == 32) this.attack.toggle(pressed);
    if (ke.getKeyCode() == 17) this.attack.toggle(pressed);
    if (ke.getKeyCode() == 96) this.attack.toggle(pressed);
    if (ke.getKeyCode() == 155) this.attack.toggle(pressed);
    if (ke.getKeyCode() == 10) this.menu.toggle(pressed);

    if (ke.getKeyCode() == 88) this.menu.toggle(pressed);
    if (ke.getKeyCode() == 67) this.attack.toggle(pressed);
    if (ke.getKeyCode() == 69) this.menu.toggle(pressed);
  }

  public void keyTyped(KeyEvent ke)
  {
  }

  public class Key
  {
    public int presses;
    public int absorbs;
    public boolean down;
    public boolean clicked;

    public Key()
    {
      InputHandler.this.keys.add(this);
    }

    public void toggle(boolean pressed) {
      if (pressed != this.down) {
        this.down = pressed;
      }
      if (pressed)
        this.presses += 1;
    }

    public void tick()
    {
      if (this.absorbs < this.presses) {
        this.absorbs += 1;
        this.clicked = true;
      } else {
        this.clicked = false;
      }
    }
  }
}
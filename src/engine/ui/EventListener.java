package engine.ui;

/**
 * A UI event listener (button presses, mostly)
 * @author ricardo
 */
public interface EventListener {
    public abstract void onEvent(String elementName, EventType et);
}
package ui.async;

@FunctionalInterface
public interface ProgressUpdate {
    public void update(int percentage);
}

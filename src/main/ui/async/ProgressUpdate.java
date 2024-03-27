package ui.async;

// EXPERIMENTAL, Not yet used, a function to set progressBar percentage
@FunctionalInterface
public interface ProgressUpdate {
    void update(int percentage);
}

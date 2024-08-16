package application.types;

class HistoryStacks<T> {
    public CustomStack<T> undoStack;
    public CustomStack<T> redoStack;

    public HistoryStacks() {
        undoStack = new CustomStack<>();
        redoStack = new CustomStack<>();
    }

    public void push(T action) {
        if (undoStack.isEmpty() || !undoStack.peek().equals(action)) {
            undoStack.push(action);
            redoStack.clear();
        }
    }

    public T undo() {
        if (!undoStack.isEmpty()) {
            T action = undoStack.pop();
            redoStack.push(action);
            return action;
        }
        return null;
    }

    public T redo() {
        if (!redoStack.isEmpty()) {
            T action = redoStack.pop();
            undoStack.push(action);
            return action;
        }
        return null;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    public T peek() {
        if (!undoStack.isEmpty()) {
            return undoStack.peek();
        }
        return null;
    }
    
    public boolean isEmpty() {
        return undoStack.isEmpty();
    }
}

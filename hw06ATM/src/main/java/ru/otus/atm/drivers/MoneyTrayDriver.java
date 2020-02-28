package ru.otus.atm.drivers;

public class MoneyTrayDriver {
    private static int trayCounter = 0;

    public static int registerTray() {
        int newTrayId = trayCounter++;
        System.out.println(String.format("New money tray registered with id: %d", newTrayId));
        return newTrayId;
    }

    public static void transferNoteFromInputToTray(int trayId) {
        System.out.println(String.format("Transferring note from input device to tray with id: %d", trayId));
    }

    public static void transferNotesFromTrayToOutput(int trayId, long notesCount) {
        System.out.println(String.format("Withdrawing %d note(s) from tray with id %d", notesCount, trayId));
    }
}

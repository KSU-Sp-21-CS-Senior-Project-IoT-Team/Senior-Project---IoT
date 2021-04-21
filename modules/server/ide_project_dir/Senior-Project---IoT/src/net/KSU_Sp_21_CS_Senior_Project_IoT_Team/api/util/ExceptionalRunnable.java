package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util;

public interface ExceptionalRunnable<T extends Throwable> {
    void run() throws T;
}

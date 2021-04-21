import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.Schedule;

public class gsonstuff {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Schedule[] schedules = {
                new Schedule(0, "asdf", "jkl"),
                new Schedule(0, "asdf", "jkl"),
                new Schedule(0, "asdf", "jkl"),
                new Schedule(0, "asdf", "jkl")
        };

    }
}

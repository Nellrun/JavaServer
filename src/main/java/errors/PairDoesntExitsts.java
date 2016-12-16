package errors;

/**
 * Created by root on 12/16/16.
 */
public class PairDoesntExitsts extends Error {
    public PairDoesntExitsts() {
        this.errorCode = 7;
        this.errorName = "Пара не найдена";
        this.errorText = "Ошибка. У заданной группы в данное время нет пары";
    }
}

package errors;

/**
 * Created by root on 12/16/16.
 */
public class PairAlreadyExistsError extends Error {

    public PairAlreadyExistsError() {
        this.errorCode = 6;
        this.errorName = "Пара уже существует";
        this.errorText = "Ошибка. У данной группы в этой время уже есть пара";
    }
}

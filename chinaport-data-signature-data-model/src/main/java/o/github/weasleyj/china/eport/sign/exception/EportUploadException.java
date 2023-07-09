package o.github.weasleyj.china.eport.sign.exception;

/**
 * Upload Eport Exception
 */
public class EportUploadException extends RuntimeException {
    private int code;

    public EportUploadException() {
        super("Upload to eport error.");
        this.code = 500;
    }

    public EportUploadException(int code) {
        super("Upload to eport error.");
        this.code = code;
    }

    public EportUploadException(String message) {
        super(message);
        this.code = 500;
    }

    public EportUploadException(int code, String message) {
        super(message);
        this.code = code;
    }

    public EportUploadException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public EportUploadException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

package o.github.weasleyj.eport.sign.exception;

/**
 * Upload Eport Exception
 */
public class UploadEportException extends RuntimeException {
    private int code;

    public UploadEportException() {
        super("Upload to eport error.");
        this.code = 500;
    }

    public UploadEportException(int code) {
        super("Upload to eport error.");
        this.code = code;
    }

    public UploadEportException(String message) {
        super(message);
        this.code = 500;
    }

    public UploadEportException(int code, String message) {
        super(message);
        this.code = code;
    }

    public UploadEportException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public UploadEportException(int code, Throwable cause) {
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

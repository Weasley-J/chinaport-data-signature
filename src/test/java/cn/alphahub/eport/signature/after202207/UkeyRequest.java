package cn.alphahub.eport.signature.after202207;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Map;


/**
 * UkeyRequest
 *
 * @author weasley
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UkeyRequest implements Serializable {
    private String _method;
    private int _id = 1;
    @Nullable
    private Map<String, Object> args;

    public UkeyRequest(String _method, @Nullable Map<String, Object> args) {
        this._method = _method;
        this.args = args;
    }
}

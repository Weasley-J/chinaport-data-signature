package o.github.weasleyj.china.eport.sign.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public final class GUIDUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final String[] chars = new String[]{
            "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"
    };

    private static String shortUUID(int length) {
        if (length <= 0) {
            length = 8;
        }
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < length; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 获取GUID
     *
     * @return GUID
     */
    public static String getGuid() {
        String first = shortUUID(6).toUpperCase();
        String second = "WEASLEY";
        String third = LocalDateTime.now().format(FORMATTER);
        String forth = shortUUID(6).toUpperCase();

        return first.concat("-")
                .concat(second)
                .concat("-")
                .concat(third)
                .concat("-")
                .concat(forth);
    }

}

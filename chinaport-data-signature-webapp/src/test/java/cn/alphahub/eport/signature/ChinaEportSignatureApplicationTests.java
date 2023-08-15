package cn.alphahub.eport.signature;

import cn.alphahub.dtt.plus.util.JacksonUtil;
import cn.alphahub.eport.signature.support.OriginalPropertyNamingStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.weasleyj.china.eport.sign.model.request.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * China Eport Signature Application Tests
 */
@Slf4j
@SpringBootTest
class ChinaEportSignatureApplicationTests {

    @Test
    void contextLoads() {
        log.info("Hello, China Eport Signature App!");
    }

    @Test
    void testJsonDeserialize() throws Exception {
        String json = """
                {
                  "Message": {
                    "MessageHead": {
                      "MessageId": "",
                      "MessageType": "CEB311Message.xml",
                      "SenderID": "SHOP",
                      "ReceiverID": "PORT",
                      "SendTime": "2023-08-07 22:45:25",
                      "Version": "1.0"
                    },
                    "MessageBody": {
                      "data": "PGNlYjpDRUIzMTFNZXNzYWdlIGd1aWQ9IjNTRkY5Ti1XRUFTTEVZLTIwMjMwODA3MjI0NTI1LUxWSkwxTSIgdmVyc2lvbj0iMS4wIiB4bWxuczpjZWI9Imh0dHA6Ly93d3cuY2hpbmFwb3J0Lmdvdi5jbi9jZWIiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiID4KICAgIDxjZWI6T3JkZXI+CiAgICAgICAgPGNlYjpPcmRlckhlYWQ+CiAgICAgICAgICAgIDxjZWI6Z3VpZD4zU0ZGOU4tV0VBU0xFWS0yMDIzMDgwNzIyNDUyNS1MVkpMMU08L2NlYjpndWlkPgogICAgICAgICAgICA8Y2ViOmFwcFR5cGU+MTwvY2ViOmFwcFR5cGU+CiAgICAgICAgICAgIDxjZWI6YXBwVGltZT4yMDIzMDcwNDE4MTAyODwvY2ViOmFwcFRpbWU+CiAgICAgICAgICAgIDxjZWI6YXBwU3RhdHVzPjI8L2NlYjphcHBTdGF0dXM+CiAgICAgICAgICAgIDxjZWI6b3JkZXJUeXBlPkk8L2NlYjpvcmRlclR5cGU+CiAgICAgICAgICAgIDxjZWI6b3JkZXJObz5UX0M1MDUxNTExMzMyMTM4MTYwMDEwPC9jZWI6b3JkZXJObz4KICAgICAgICAgICAgPGNlYjplYnBDb2RlPjQ2MDE2MzAwMDQ8L2NlYjplYnBDb2RlPgogICAgICAgICAgICA8Y2ViOmVicE5hbWU+5rW35Y2X55yB6I2j6KqJ6L+b5Ye65Y+j6LS45piT5pyJ6ZmQ5YWs5Y+4PC9jZWI6ZWJwTmFtZT4KICAgICAgICAgICAgPGNlYjplYmNDb2RlPjQ2MDE2MzAwMDQ8L2NlYjplYmNDb2RlPgogICAgICAgICAgICA8Y2ViOmViY05hbWU+5rW35Y2X55yB6I2j6KqJ6L+b5Ye65Y+j6LS45piT5pyJ6ZmQ5YWs5Y+4PC9jZWI6ZWJjTmFtZT4KICAgICAgICAgICAgPGNlYjpnb29kc1ZhbHVlPjAuMDE8L2NlYjpnb29kc1ZhbHVlPgogICAgICAgICAgICA8Y2ViOmZyZWlnaHQ+MDwvY2ViOmZyZWlnaHQ+CiAgICAgICAgICAgIDxjZWI6ZGlzY291bnQ+MDwvY2ViOmRpc2NvdW50PgogICAgICAgICAgICA8Y2ViOnRheFRvdGFsPjA8L2NlYjp0YXhUb3RhbD4KICAgICAgICAgICAgPGNlYjphY3R1cmFsUGFpZD4wLjAxPC9jZWI6YWN0dXJhbFBhaWQ+CiAgICAgICAgICAgIDxjZWI6Y3VycmVuY3k+MTQyPC9jZWI6Y3VycmVuY3k+CiAgICAgICAgICAgIDxjZWI6YnV5ZXJSZWdObz40PC9jZWI6YnV5ZXJSZWdObz4KICAgICAgICAgICAgPGNlYjpidXllck5hbWU+6KKB5pmT6ZuoPC9jZWI6YnV5ZXJOYW1lPgogICAgICAgICAgICA8Y2ViOmJ1eWVyVGVsZXBob25lPjEzNzAxNzI3Mzc1PC9jZWI6YnV5ZXJUZWxlcGhvbmU+CiAgICAgICAgICAgIDxjZWI6YnV5ZXJJZFR5cGU+MTwvY2ViOmJ1eWVySWRUeXBlPgogICAgICAgICAgICA8Y2ViOmJ1eWVySWROdW1iZXI+MTMwNDM1MjAwMDA5MjQxNTM4PC9jZWI6YnV5ZXJJZE51bWJlcj4KICAgICAgICAgICAgPGNlYjpjb25zaWduZWU+6KKB5pmT6ZuoPC9jZWI6Y29uc2lnbmVlPgogICAgICAgICAgICA8Y2ViOmNvbnNpZ25lZVRlbGVwaG9uZT4xMzcwMTcyNzM3NTwvY2ViOmNvbnNpZ25lZVRlbGVwaG9uZT4KICAgICAgICAgICAgPGNlYjpjb25zaWduZWVBZGRyZXNzPuWMl+S6rOWMl+S6rOW4guS4nOWfjuWMujwvY2ViOmNvbnNpZ25lZUFkZHJlc3M+CiAgICAgICAgICAgIDxjZWI6bm90ZT50ZXN0PC9jZWI6bm90ZT4KICAgICAgICA8L2NlYjpPcmRlckhlYWQ+CiAgICAgICAgPGNlYjpPcmRlckxpc3Q+CiAgICAgICAgICAgIDxjZWI6Z251bT4xPC9jZWI6Z251bT4KICAgICAgICAgICAgPGNlYjppdGVtTm8+MTwvY2ViOml0ZW1Obz4KICAgICAgICAgICAgPGNlYjppdGVtTmFtZT5MQU5OQeWFsOe6szwvY2ViOml0ZW1OYW1lPgogICAgICAgICAgICA8Y2ViOmdtb2RlbD4xMOeJhy/ljIU8L2NlYjpnbW9kZWw+CiAgICAgICAgICAgIDxjZWI6aXRlbURlc2NyaWJlPjwvY2ViOml0ZW1EZXNjcmliZT4KICAgICAgICAgICAgPGNlYjpiYXJDb2RlPjE8L2NlYjpiYXJDb2RlPgogICAgICAgICAgICA8Y2ViOnVuaXQ+MDExPC9jZWI6dW5pdD4KICAgICAgICAgICAgPGNlYjpxdHk+MTwvY2ViOnF0eT4KICAgICAgICAgICAgPGNlYjpwcmljZT4xPC9jZWI6cHJpY2U+CiAgICAgICAgICAgIDxjZWI6dG90YWxQcmljZT4xPC9jZWI6dG90YWxQcmljZT4KICAgICAgICAgICAgPGNlYjpjdXJyZW5jeT4xNDI8L2NlYjpjdXJyZW5jeT4KICAgICAgICAgICAgPGNlYjpjb3VudHJ5PjEzNjwvY2ViOmNvdW50cnk+CiAgICAgICAgICAgIDxjZWI6bm90ZT50ZXN0PC9jZWI6bm90ZT4KICAgICAgICA8L2NlYjpPcmRlckxpc3Q+CiAgICA8L2NlYjpPcmRlcj4KICAgIDxjZWI6QmFzZVRyYW5zZmVyPgogICAgICAgIDxjZWI6Y29wQ29kZT40NjAxNjMwMDA0PC9jZWI6Y29wQ29kZT4KICAgICAgICA8Y2ViOmNvcE5hbWU+5rW35Y2X55yB6I2j6KqJ6L+b5Ye65Y+j6LS45piT5pyJ6ZmQ5YWs5Y+4PC9jZWI6Y29wTmFtZT4KICAgICAgICA8Y2ViOmR4cE1vZGU+RFhQPC9jZWI6ZHhwTW9kZT4KICAgICAgICA8Y2ViOmR4cElkPkRYUEVOVDAwMDA1MzA4MTU8L2NlYjpkeHBJZD4KICAgICAgICA8Y2ViOm5vdGU+dGVzdDwvY2ViOm5vdGU+CiAgICA8L2NlYjpCYXNlVHJhbnNmZXI+CjxkczpTaWduYXR1cmUgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPgogICAgPGRzOlNpZ25lZEluZm8+CiAgICAgICAgPGRzOkNhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy9UUi8yMDAxL1JFQy14bWwtYzE0bi0yMDAxMDMxNSIvPgogICAgICAgIDxkczpTaWduYXR1cmVNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjc20yLXNtMyIvPgogICAgICAgIDxkczpSZWZlcmVuY2UgVVJJPSIiPgogICAgICAgICAgICA8ZHM6VHJhbnNmb3Jtcz4KICAgICAgICAgICAgICAgIDxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjZW52ZWxvcGVkLXNpZ25hdHVyZSIvPgogICAgICAgICAgICA8L2RzOlRyYW5zZm9ybXM+CiAgICAgICAgICAgIDxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjc2hhMSIvPgogICAgICAgICAgICA8ZHM6RGlnZXN0VmFsdWU+VDlnMGhpR2YrT2pFZSszU1A4Y0wzVEx4eDNzPTwvZHM6RGlnZXN0VmFsdWU+CiAgICAgICAgPC9kczpSZWZlcmVuY2U+CiAgICA8L2RzOlNpZ25lZEluZm8+CiAgICA8ZHM6U2lnbmF0dXJlVmFsdWU+SUxGZDhINjRHL0VYalZ1ZjIvTGdLeXhhK3hBazlTS1Jjb1owL3prY2tGbmhyR052eUhud1RJL0l4dHJ4cjhLMDl5dFFhQlZQeEJBeS9RMFdxZVNQSFE9PTwvZHM6U2lnbmF0dXJlVmFsdWU+CiAgICA8ZHM6S2V5SW5mbz4KICAgICAgICA8ZHM6S2V5TmFtZT4wMzAwMDAwMDAwMGNkZTZmPC9kczpLZXlOYW1lPgogICAgICAgIDxkczpYNTA5RGF0YT4KICAgICAgICAgICAgPGRzOlg1MDlDZXJ0aWZpY2F0ZT5NSUlFb0RDQ0JFU2dBd0lCQWdJSUF3QUFBQUFNM204d0RBWUlLb0VjejFVQmczVUZBRENCbURFTE1Ba0dBMVVFQmhNQ1EwNHhEekFOQmdOVkJBZ01CdVdNbCtTNnJERVBNQTBHQTFVRUJ3d0c1WXlYNUxxc01Sc3dHUVlEVlFRS0RCTGt1SzNsbTczbmxMWGxyWkRsajZQbHNyZ3hHekFaQmdOVkJBc01FdWl2Z2VTNXB1ZXVvZWVRaHVTNHJlVy9nekV0TUNzR0ExVUVBd3drNUxpdDVadTk1NVMxNWEyUTVMaWE1WXFoNksrQjVMbW01NjZoNTVDRzVMaXQ1YitETUI0WERUSXpNRE15T1RBd01EQXdNRm9YRFRNek1ETXlPVEF3TURBd01Gb3dWakVMTUFrR0ExVUVCaE1DUTA0eE16QXhCZ05WQkFzTUt1YTF0K1dObCtlY2dlaU5vK2lxaWVpL20rV0h1dVdQbytpMHVPYVlrK2FjaWVtWmtPV0ZyT1dQdURFU01CQUdBMVVFQXd3SjVwMm81YWFDNlllUk1Ga3dFd1lIS29aSXpqMENBUVlJS29FY3oxVUJnaTBEUWdBRTB2T1FtcGxBcjlpZ1BackE4RjFtc3FuRmQwVSsrNkc2TmhHNXJOdUlVV2Z0MEJ3UW43ZVNKa3Q1L2Z2U1NvZTdwVWcyL2F3SFVXUG56a2VlUWM3b1ZxT0NBclV3Z2dLeE1CRUdDV0NHU0FHRytFSUJBUVFFQXdJRm9EQU9CZ05WSFE4QkFmOEVCQU1DQnNBd0NRWURWUjBUQkFJd0FEQXBCZ05WSFNVRUlqQWdCZ2dyQmdFRkJRY0RBZ1lJS3dZQkJRVUhBd1FHQ2lzR0FRUUJnamNVQWdJd0h3WURWUjBqQkJnd0ZvQVVSQ1F4dDB3RXZvQVZYbXVvNE4xYmpLWFRoMFV3SFFZRFZSME9CQllFRkF5dEdvYjVMMFdxaE9DWjVsNkxmMmpVZE5yQU1HZ0dBMVVkSUFSaE1GOHdYUVlFVlIwZ0FEQlZNRk1HQ0NzR0FRVUZCd0lCRmtkb2RIUndjem92TDNkM2R5NWphR2x1WVhCdmNuUXVaMjkyTG1OdUwzUmpiWE5tYVd4bEwzVXZZMjF6TDNkM2R5OHlNREl5TURRdk1USXhNekk1TkRoNGREWndMbkJrWmpCL0JnTlZIUjhFZURCMk1IU2djcUJ3aG01c1pHRndPaTh2YkdSaGNDNWphR2x1WVhCdmNuUXVaMjkyTG1OdU9qTTRPUzlqYmoxamNtd3dNekF3TURBc2IzVTlZM0pzTURBc2IzVTlZM0pzTEdNOVkyNC9ZMlZ5ZEdsbWFXTmhkR1ZTWlhadlkyRjBhVzl1VEdsemREOWlZWE5sUDJOdVBXTnliREF6TURBd01EQStCZ2dyQmdFRkJRY0JBUVF5TURBd0xnWUlLd1lCQlFVSE1BR0dJbWgwZEhBNkx5OXZZM053TG1Ob2FXNWhjRzl5ZEM1bmIzWXVZMjQ2T0Rnd01DOHdPZ1lLS3dZQkJBR3BRMlFGQVFRc0RDcm10YmZsalpmbm5JSG9qYVBvcW9ub3Y1dmxoN3JsajZQb3RMam1tSlBtbklucG1aRGxoYXpsajdnd0VnWUtLd1lCQkFHcFEyUUZBd1FFREFJd01UQWlCZ29yQmdFRUFhbERaQVVJQkJRTUVqVXhNak15TkRFNU5qUXhNREUzTWprM1dEQWdCZ29yQmdFRUFhbERaQVVKQkJJTUVEQXpMVXBLTUVjNU1EQXlNakEzTlRJd0dRWUtLd1lCQkFHcFEyUUZDd1FMREFsTlFUVlVUa1pIV1Rrd0VnWUtLd1lCQkFHcFEyUUZEQVFFREFJd01EQVNCZ29yQmdFRUFhbERaQUlCQkFRTUFqRXlNQklHQ2lzR0FRUUJxVU5rQWdRRUJBd0NNVFF3REFZSUtvRWN6MVVCZzNVRkFBTklBREJGQWlCTTRPVkFjOGFhQ1pVNFhGZmNWTWtDN2JXSUllblJuUEx4cm53VmVZTzNDUUloQU5RNzY3WUl1cmtKQ29MdHd5cVFQYlVaZS8rM0JqR1pjSVdxQjFtQWw5VCs8L2RzOlg1MDlDZXJ0aWZpY2F0ZT4KICAgICAgICA8L2RzOlg1MDlEYXRhPgogICAgPC9kczpLZXlJbmZvPgo8L2RzOlNpZ25hdHVyZT4KPC9jZWI6Q0VCMzExTWVzc2FnZT4\\u003d"
                    }
                  }
                }
                """;
        ObjectMapper objectMapper = JacksonUtil.MAPPER.copy();
        objectMapper.setPropertyNamingStrategy(new OriginalPropertyNamingStrategy());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        MessageRequest messageRequest = objectMapper.readValue(json, MessageRequest.class);
        System.out.println(objectMapper.writeValueAsString(messageRequest));
    }
}

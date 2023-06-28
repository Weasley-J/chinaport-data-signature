package cn.alphahub.eport.signature.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令行客户端工具类
 *
 * @author weasley
 */

public final class CommandClient {
    /**
     * 默认共享示例，延迟加载
     */
    private static volatile CommandClient sharedInstance;
    private final Logger log = LoggerFactory.getLogger(CommandClient.class);
    /**
     * 保存进程的输入流信息
     */
    private final List<String> stdOutList = new ArrayList<>();
    /**
     * 保存进程的错误流信息
     */
    private final List<String> errorOutList = new ArrayList<>();

    private CommandClient() {
    }

    /**
     * Return a shared default {@code CommandClient} instance,
     * lazily building it once needed.
     * the shared {@code CommandClient} instance (never {@code null})
     *
     * @return the shared {@code CommandClient} instance (never {@code null})
     */
    public static CommandClient getSharedInstance() {
        CommandClient commandClient = sharedInstance;
        if (commandClient == null) {
            synchronized (CommandClient.class) {
                commandClient = sharedInstance;
                if (commandClient == null) {
                    commandClient = new CommandClient();
                    sharedInstance = commandClient;
                }
            }
        }
        return commandClient;
    }

    /**
     * 执行命令脚本
     *
     * @param commandScripts 多行命令行
     * @apiNote 支持大部分平台: MacOSX | Linux | Windows
     */
    public void execute(String... commandScripts) {
        for (String commandScript : commandScripts) {
            execute(commandScript);
        }
    }

    /**
     * 执行命令
     *
     * @param command 命令行
     * @apiNote 支持大部分平台: MacOSX | Linux | Windows
     */
    @SuppressWarnings({"all"})
    public void execute(String command) {
        if (null == command || command.isBlank() || command.isEmpty()) return;
        if (log.isDebugEnabled()) {
            log.debug("\n{}", command);
        }
        // 先清空
        stdOutList.clear();
        errorOutList.clear();
        try {
            Process process = Runtime.getRuntime().exec(command);
            // 创建2个线程，分别读取输入流缓冲区和错误流缓冲区
            ThreadWrapper stdOutWrapper = new ThreadWrapper(stdOutList, process.getInputStream());
            ThreadWrapper errorOutWrapper = new ThreadWrapper(errorOutList, process.getErrorStream());
            //启动线程读取缓冲区数据
            stdOutWrapper.start();
            errorOutWrapper.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error("Execute command error message: {}", e.getLocalizedMessage());
            throw new CommandClientException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 执行命令脚本
     *
     * @param command              命令行
     * @param redirectOutputStream 将控制台输出重定向到输出流
     * @apiNote 支持大部分平台: MacOSX | Linux | Windows, 结束会后关闭流
     */
    @SuppressWarnings({"all"})
    public void execute(String command, OutputStream redirectOutputStream) {
        if (null == command || command.isBlank() || command.isEmpty()) return;
        if (log.isDebugEnabled()) {
            log.debug("\n{}", command);
        }
        try {
            Process process = Runtime.getRuntime().exec(command);
            // 创建2个线程，读取输入流缓冲区和错误流缓冲区并将输入流重定向到 outputStream, 启动线程读取缓冲区数据
            new RedirectOutputStreamWrapper(process.getInputStream(), redirectOutputStream).start();
            new RedirectOutputStreamWrapper(process.getErrorStream(), redirectOutputStream).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error("Execute command error message: {}", e.getLocalizedMessage(), e);
        } finally {
            if (null != redirectOutputStream) {
                try {
                    redirectOutputStream.close();
                } catch (IOException e) {
                    log.error("Execute command error message: {}", e.getLocalizedMessage(), e);
                }
            }
        }
    }

    public List<String> getStdOutList() {
        return stdOutList;
    }

    public List<String> getErrorOutList() {
        return errorOutList;
    }
}

class ThreadWrapper implements Runnable {
    private final Logger log = LoggerFactory.getLogger(ThreadWrapper.class);

    private final List<String> outputLines;
    private final InputStream inputStream;

    ThreadWrapper(List<String> outputLines, InputStream inputStream) {
        this.outputLines = outputLines;
        this.inputStream = inputStream;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.setDaemon(true); //将其设置为守护线程
        thread.start();
    }

    @Override
    @SuppressWarnings({"all"})
    public void run() {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            if ((line = br.readLine()) != null) {
                do {
                    if (line != null) {
                        outputLines.add(line);
                        System.out.println(line);
                    }
                } while ((line = br.readLine()) != null);
            }
        } catch (IOException e) {
            log.error("Execute command error message: {}", e.getLocalizedMessage());
            throw new CommandClientException(e.getLocalizedMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("Execute command error message: {}", e.getLocalizedMessage());
                throw new CommandClientException(e.getLocalizedMessage(), e);
            }
        }
    }
}

class RedirectOutputStreamWrapper implements Runnable {
    private final Logger log = LoggerFactory.getLogger(RedirectOutputStreamWrapper.class);
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public RedirectOutputStreamWrapper(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            if ((line = br.readLine()) != null) {
                do {
                    outputStream.write(line.concat(System.getProperty("line.separator")).getBytes());
                } while ((line = br.readLine()) != null);
            }
        } catch (IOException e) {
            log.error("Execute command error message: {}", e.getLocalizedMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("Execute command error message: {}", e.getLocalizedMessage(), e);
            }
        }
    }
}

/**
 * Command Client Exception
 */
class CommandClientException extends RuntimeException {

    public CommandClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

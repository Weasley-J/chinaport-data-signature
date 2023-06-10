package cn.alphahub.eport.signature.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令行工具类
 *
 * @author weasley
 */
public final class CommandUtil {
    /**
     * 默认共享示例，延迟加载
     */
    private static volatile CommandUtil sharedInstance;
    private final Logger log = LoggerFactory.getLogger(CommandUtil.class);
    /**
     * 保存进程的输入流信息
     */
    private final List<String> stdOutList = new ArrayList<>();
    /**
     * 保存进程的错误流信息
     */
    private final List<String> errorOutList = new ArrayList<>();

    private CommandUtil() {
    }

    /**
     * Return a shared default {@code CommandUtil} instance,
     * lazily building it once needed.
     * the shared {@code CommandUtil} instance (never {@code null})
     *
     * @return the shared {@code CommandUtil} instance (never {@code null})
     */
    public static CommandUtil getSharedInstance() {
        CommandUtil commandUtil = sharedInstance;
        if (commandUtil == null) {
            synchronized (CommandUtil.class) {
                commandUtil = sharedInstance;
                if (commandUtil == null) {
                    commandUtil = new CommandUtil();
                    sharedInstance = commandUtil;
                }
            }
        }
        return commandUtil;
    }

    /**
     * 执行命令
     *
     * @param command 命令行
     * @apiNote 支持大部分平台: MacOSX | Linux | Windows
     */
    @SuppressWarnings({"all"})
    public void execute(String command) {
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
            log.error("Execute command error message: {}", e.getLocalizedMessage(), e);
        }
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

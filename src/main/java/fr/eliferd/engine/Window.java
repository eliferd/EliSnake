package fr.eliferd.engine;

import fr.eliferd.engine.input.Keyboard;
import fr.eliferd.engine.renderer.Render;
import org.joml.Vector2i;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Map;

import static java.util.Map.entry;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window _INSTANCE = null;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String TITLE = "EliSnake";
    private long _currentGlfwWindow = NULL;
    private final Render _renderer = new Render();
    private final Map<Integer,Integer> windowHintsMap = Map.ofEntries(
            entry(GLFW_RESIZABLE, GLFW_FALSE),
            entry(GLFW_CONTEXT_VERSION_MAJOR, 4),
            entry(GLFW_CONTEXT_VERSION_MINOR, 4),
            entry(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    );

    public void initWindow() {
        GLFWErrorCallback.createPrint(System.err).set();

        // Window lib initialization
        if (!glfwInit()) {
            System.err.println("[ERROR] GLFW Initialization failed.");
        }

        // Setting the GLFW window hints
        windowHintsMap.forEach(GLFW::glfwWindowHint);

        this._currentGlfwWindow = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);

        if (this._currentGlfwWindow == NULL) {
            System.err.println("[ERROR] Window build failed. Closing app.");
            glfwTerminate();
        }

        // Centering the window
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null) {
            glfwSetWindowPos(this._currentGlfwWindow, (vidMode.width() / 2) - (WIDTH / 2), (vidMode.height() / 2) - (HEIGHT / 2));
        }

        // Bind OpenGL context
        glfwMakeContextCurrent(this._currentGlfwWindow);
        GL.createCapabilities();

        this.printVersionLog();
        this.setViewportSize();
        this.runGameLoop();
    }

    /**
     * Gets the viewport width & height
     */
    public Vector2i getViewPortSize() {
        int[] viewportData = {0, 0, 0, 0};
        glGetIntegerv(GL_VIEWPORT, viewportData);
        return new Vector2i(viewportData[2], viewportData[3]);
    }

    /**
     * Print version log for debugging
     */
    private void printVersionLog() {
        System.out.println("[GLFW] " + glfwGetVersionString());
        System.out.println("[OpenGL] " + glGetString(GL_VERSION));
        System.out.println("[LWJGL] " + Version.getVersion());
    }

    /**
     * Setting the viewport size for OpenGL operations
     */
    private void setViewportSize() {
        int[] width = {0};
        int[] height = {0};
        glfwGetFramebufferSize(this._currentGlfwWindow, width, height);
        glViewport(0, 0, width[0], height[0]);
    }

    /**
     * Runs the game loop
     */
    private void runGameLoop() {
        this._renderer.init();
        float lastTime = (float)glfwGetTime();

        while(!glfwWindowShouldClose(this._currentGlfwWindow)) {
            glClearColor(0.3f, 0.6f, 0.3f, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float currentTime = (float)glfwGetTime();
            float deltaTime = currentTime - lastTime;

            if (deltaTime >= 0) {
                this._renderer.render(deltaTime);
            }

            lastTime = currentTime;

            glfwSwapBuffers(this._currentGlfwWindow);
            glfwPollEvents();
        }
        glfwTerminate();
    }

    public long getGlfwWindow() {
        return this._currentGlfwWindow;
    }

    public static Window getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new Window();
        }
        return _INSTANCE;
    }
}

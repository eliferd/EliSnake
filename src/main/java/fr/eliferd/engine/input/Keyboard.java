package fr.eliferd.engine.input;

import fr.eliferd.engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Keyboard {
    public static boolean isKeyDown(int keycode) {
        return glfwGetKey(Window.getInstance().getGlfwWindow(), keycode) == GLFW_PRESS;
    }
}

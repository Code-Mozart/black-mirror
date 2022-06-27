package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.model.ToDoEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.List;
import java.util.*;

/**
 * This class represents a widget used for showing tasks that are to do on the users mirror.
 *
 * @author Selim Özdemir
 * @author Markus Marewitz
 * @version 2022-05-19
 */
public class TodosWidget extends AbstractWidget {

    private static final int MAX_TODO_LIST_SIZE = 5;

    private final ResourceBundle resources = ResourceBundle.getBundle(
            "lang/TodosWidget", Locale.getDefault());
    private JLabel noEntriesLabel;
    private DefaultListModel<String> todoListModel;
    private JList<String> todoList;
    private JPanel panelMain;
    private GridBagConstraints gbc;

    public TodosWidget() {
        //this.setSize(300, 300);
        initComponents();
    }

    /**
     * Initiates all UI-components such as the label and the list.
     */
    private void initComponents() {
        panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(Color.BLACK);
        panelMain.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel(resources.getString("todoTitle"));
        titleLabel.setFont(new Font(WidgetFont.STANDARD_FONT.getFontName(), WidgetFont.STANDARD_FONT.getFontStyle(), 20));
        titleLabel.setForeground(Color.WHITE);
        Font font = titleLabel.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        titleLabel.setFont(font.deriveFont(attributes));

        JLabel iconLabel = new JLabel("", SwingConstants.CENTER);
        // icon author @mallorysartwork
        ImageIcon todoListIcon = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/icons/todo_list_icon.jpg")));
        iconLabel.setIcon(todoListIcon);

        titleLabel.setVerticalAlignment(SwingConstants.NORTH);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        panelMain.add(iconLabel, gbc);
        gbc.gridy = 1;
        panelMain.add(titleLabel, gbc);

        gbc.gridy = 2;

        noEntriesLabel = new JLabel(resources.getString("todoEmpty"));
        noEntriesLabel.setBackground(Color.BLACK);
        noEntriesLabel.setForeground(Color.WHITE);
        panelMain.add(noEntriesLabel, gbc);

        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        todoList.setBackground(Color.BLACK);
        todoList.setForeground(Color.WHITE);
        todoList.setFont(new Font(WidgetFont.STANDARD_FONT.getFontName(), WidgetFont.STANDARD_FONT.getFontStyle(), 16));
        panelMain.add(todoList, gbc);

        this.add(panelMain);
    }

    /**
     * Formats an entry to the outputted string.
     *
     * @param entry ToDoEntry
     * @return parsed reminder information as a String
     */
    private String formatTodo(ToDoEntry entry) {
        String text = entry.text();
        if (text.length() > 30) {
            text = text.substring(0, 29) + "...";
        }
        return "- " + text;
    }

    public void setEntries(List<ToDoEntry> entries) {
        if (entries.isEmpty()) {
            todoList.setVisible(false);
            noEntriesLabel.setVisible(true);
        } else {
            noEntriesLabel.setVisible(false);
            todoListModel.removeAllElements();

            for (ToDoEntry entry : entries) {
                if (todoListModel.size() == MAX_TODO_LIST_SIZE - 1) {
                    if (entries.size() > MAX_TODO_LIST_SIZE) {
                        todoListModel.addElement("...");
                    } else {
                        todoListModel.addElement(formatTodo(entry));
                    }
                    break;
                } else {
                    todoListModel.addElement(formatTodo(entry));
                }
            }
            todoList.setVisible(true);
        }
    }
}

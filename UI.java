import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UI extends Thread {
    JFrame root;
    Map<String, JComponent> components;
    Object[][] tableData;
    String[] tableColumns;
    ThreadCounter threadHolder;

    public UI(ThreadCounter tr) {
        components = new HashMap<String, JComponent>();
        threadHolder = tr;
        createComponents();
        drawComponents();

        root.setVisible(true);
    }

    private void createComponents() {
        root=new JFrame();
        components.put("lThreadNum", new JLabel("Thread number"));
        components.put("eThreadNum", new JTextField(15));
        components.put("bStart", new JButton("Start"));
        ((JButton)components.get("bStart")).addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                
            }
        });

        tableColumns = new String[] {"Name", "State", "Priority", "isActive"};
        components.put("tThreads", new JTable(new DefaultTableModel(0, 0)));

        root.add(components.get("lThreadNum"));
        root.add(components.get("eThreadNum"));
        root.add(components.get("bStart"));
        root.add(components.get("tThreads"));
    }

    private void drawComponents() {
        root.setSize(400,500);
        components.get("lThreadNum").setBounds(15, 15, 100, 30);
        components.get("eThreadNum").setBounds(130, 15, 100, 30);
        components.get("bStart").setBounds(260, 15, 100, 30);
        components.get("tThreads").setBounds(15, 50, 360, 300);
        root.setLayout(null);
    }

    public void run() {
        while(true) {
            var threads = threadHolder.getThreads();
            tableData = new Object[threads.size()][4];
            for(int i = 0; i < threads.size(); i++) {
                tableData[i] = new Object[] {
                    threads.get(i).getName(),
                    threads.get(i).getState(),
                    threads.get(i).getPriority(),
                    threads.get(i).isAlive()
                };
            }
            updateTable(tableData);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) { System.out.println("Interrupted"); }
        }
    }

    private void updateTable(Object[][] data) {
        var table = (JTable)components.get("tThreads");
        var model = (DefaultTableModel)table.getModel();
        int num = table.getModel().getRowCount();
        for(int i = 0; i < num; ++i) {
            model.removeRow(0);
        }
        model.setRowCount(data.length);
        if(data.length > 0) {
            model.setColumnCount(data[0].length);
            for(int i = 0; i < data.length; ++i) {
                for(int j = 0; j < data[i].length; ++j) {
                    model.setValueAt(data[i][j], i, j);
                }
            }
        }
    }
}

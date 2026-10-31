package vn.edu.ptit.oop.nhom08.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class PayrollCellRenderer extends DefaultTableCellRenderer {
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        
        if (value instanceof Double amount) {
            setText(currencyFormat.format(amount));
            setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            setText(value != null ? value.toString() : "");
            if (column == 0 || column == 2) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
        }

       
        if (!isSelected) {
            if (row % 2 == 0) {
                c.setBackground(Color.WHITE);
            } else {
                c.setBackground(new Color(240, 248, 255)); 
            }

            
            if (column == 6) {
                c.setFont(c.getFont().deriveFont(Font.BOLD));
                c.setForeground(new Color(0, 100, 0));
            } else {
                c.setFont(c.getFont().deriveFont(Font.PLAIN));
                c.setForeground(Color.BLACK);
            }
        } else {
            
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        }

        return c;
    }
}

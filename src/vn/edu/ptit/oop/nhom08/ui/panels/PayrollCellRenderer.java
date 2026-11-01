package vn.edu.ptit.oop.nhom08.ui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import vn.edu.ptit.oop.nhom08.util.CurrencyFormatUtil;

// Renderer căn lề, định dạng tiền VNĐ và tô màu nổi bật dòng tổng cộng bảng lương
public class PayrollCellRenderer extends DefaultTableCellRenderer {

    private static final Color STRIPE_COLOR = new Color(248, 250, 252);
    private static final Color SUMMARY_BG = new Color(254, 243, 199);
    private static final Color SUMMARY_BORDER = new Color(245, 158, 11);
    private static final Color NET_SALARY_COLOR = new Color(22, 101, 52);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        boolean isSummary = false;
        if (table.getModel() instanceof PayrollTableModel) {
            isSummary = ((PayrollTableModel) table.getModel()).isSummaryRow(row);
        }

        // Định dạng hiển thị và căn lề theo kiểu dữ liệu
        if (value instanceof Double) {
            setText(CurrencyFormatUtil.format((Double) value));
            setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            setText(value != null ? value.toString() : "");
            if (column == 0 || column == 1) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
        }

        // Thiết lập màu sắc và kiểu chữ
        if (isSummary) {
            c.setFont(table.getFont().deriveFont(Font.BOLD));
            if (!isSelected) {
                c.setBackground(SUMMARY_BG);
                c.setForeground(Color.BLACK);
                if (c instanceof javax.swing.JComponent) {
                    ((javax.swing.JComponent) c).setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, SUMMARY_BORDER));
                }
            }
        } else if (!isSelected) {
            c.setFont(table.getFont().deriveFont(Font.PLAIN));
            c.setBackground((row % 2 == 0) ? Color.WHITE : STRIPE_COLOR);

            // Cột Thực lĩnh: in đậm màu xanh lá nổi bật
            if (column == 7) {
                c.setFont(table.getFont().deriveFont(Font.BOLD));
                c.setForeground(NET_SALARY_COLOR);
            } else {
                c.setForeground(Color.BLACK);
            }
        }

        return c;
    }
}

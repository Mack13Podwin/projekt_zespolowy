import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class SwingWindow extends JFrame implements IView{
    JLabel label;
    JLabel codeLabel;
    JLabel lastCodeLabel;
    public SwingWindow(){
        label = new JLabel(new ImageIcon());
        //Set Content to the JFrame
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        codeLabel=new JLabel();
        codeLabel.setText("no code yet");
        lastCodeLabel=new JLabel("");
        panel.add(label);
        panel.add(codeLabel);
        panel.add(lastCodeLabel);
        getContentPane().add(panel);
        pack();
        setVisible(true);
    }
    public void refreshFrame(BufferedImage img) {
        label.setIcon(new ImageIcon(img));
    }


    @Override
    public void setCurrentCode(String currentCode) {
        codeLabel.setText(currentCode);
    }

    @Override
    public void setLastCode(String lastCode) {
        lastCodeLabel.setText(lastCode);
    }

    @Override
    public void setCurrentRed() {
        codeLabel.setForeground(Color.RED);
    }

    @Override
    public void setCurrentGreen() {
        codeLabel.setForeground(Color.GREEN);
    }

    @Override
    public String getCurrent() {
        return codeLabel.getText();
    }
}

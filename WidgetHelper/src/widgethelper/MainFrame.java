/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package widgethelper;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileReader;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 *
 * @author EricGummerson
 */
public class MainFrame extends javax.swing.JFrame {

    DefaultListModel list;
    String[] listString;
    int[] xInc, yInc, ioIds;
    boolean gen = false;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    public void update() {
        gen = false;
        if (checkPos()) {
            if (checkIos()) {

                list = new DefaultListModel();
                listString = new String[ioIds.length];
                int row = Integer.parseInt(ftf_Rows.getText());
                int col = Integer.parseInt(ftf_Cols.getText());
                int startX = Integer.parseInt(ftf_startXpos.getText());
                int startY = Integer.parseInt(ftf_startYpos.getText());
                if (rb_down.isSelected()) {
                    // down then right
                    String line;
                    int xIncrement, yIncrement;
                    int count = 0;
                    for (int c = 0; c < col; c++) {
                        // Get the x increment
                        if (c == 0) {
                            xIncrement = 0;
                        } else if (xInc.length == 1) {
                            xIncrement = xInc[0];
                        } else {
                            if (c < xInc.length) {
                                xIncrement = xInc[c - 1];
                            } else {
                                xIncrement = xInc[xInc.length - 1];

                            }
                        }
                        for (int r = 0; r < row; r++) {

                            if (count < ioIds.length) {
                                // Get the y increment
                                if (r == 0) {
                                    yIncrement = 0;
                                } else if (yInc.length == 1) {
                                    yIncrement = yInc[0];
                                } else {
                                    if (r < yInc.length) {
                                        yIncrement = yInc[r - 1];
                                    } else {
                                        yIncrement = yInc[yInc.length - 1];
                                    }
                                }
                                line = String.valueOf(ioIds[count]) + ",";
                                line += String.valueOf(startX + xIncrement) + ",";
                                line += String.valueOf(startY + yIncrement);
                                listString[count] = line;
                                list.addElement(line);
                                startY += yIncrement;
                                count++;
                            } else {
                                //System.out.println("Ios: " + ioIds.length + "\tCount: " + count);
                            }
                        }
                        
                        startX += xIncrement;
                        startY = Integer.parseInt(ftf_startYpos.getText());
                        
                    }
                } else {
                    // right then down
                    String line;
                    int xIncrement, yIncrement;
                    int count = 0;

                    for (int r = 0; r < row; r++) {
                        // Get the x increment
                        // Get the y increment
                        if (r == 0) {
                            yIncrement = 0;
                        } else if (yInc.length == 1) {
                            yIncrement = yInc[0];
                        } else {
                            if (r < yInc.length) {
                                yIncrement = yInc[r - 1];
                            } else {
                                yIncrement = yInc[yInc.length - 1];
                            }
                        }

                        for (int c = 0; c < col; c++) {

                            if (count < ioIds.length) {
                                if (c == 0) {
                                    xIncrement = 0;
                                } else if (xInc.length == 1) {
                                    xIncrement = xInc[0];
                                } else {
                                    if (c < xInc.length) {
                                        xIncrement = xInc[c - 1];
                                    } else {
                                        xIncrement = xInc[xInc.length - 1];

                                    }
                                }
                                line = String.valueOf(ioIds[count]) + ",";
                                line += String.valueOf(startX + xIncrement) + ",";
                                line += String.valueOf(startY + yIncrement);
                                listString[count] = line;
                                list.addElement(line);
                                startX += xIncrement;
                                count++;
                            } else {
                                //System.out.println("Ios: " + ioIds.length + "\tCount: " + count);
                            }
                        }
                        
                        startX = Integer.parseInt(ftf_startXpos.getText());
                        startY += yIncrement;
                       
                    }
                }

                list_io.setModel(list);
                gen = true;

            } else {
                System.out.println("make ios");
            }
        } else {
            System.out.println("Make positions");
        }

    }

    public boolean checkIos() {

        String text = tf_ios.getText();
        
        System.out.println(text.split("\n")[0]);
        if (!text.isEmpty()) {
            list = new DefaultListModel();

            int numIos = Integer.parseInt(ftf_Ios.getText());
            String[] ios;
            if(text.contains(",")){
                ios = text.split(",");
            }else {
                ios = text.split("\n");
            }
           
            // Generate a list
            if (ios.length == 1 && numIos != 1) {
                System.out.println("Generate multiple IOS variable");
                int startIo = Integer.parseInt(ios[0]);
                ioIds = new int[numIos];
                for (int i = 0; i < numIos; i++) {
                    ioIds[i] = (startIo + i);
                    list.addElement(startIo + i);
                }
            } else if (ios.length == 1) {
                System.out.println("1 IO");
                ioIds = new int[numIos];                
                ioIds[0] = Integer.parseInt(ios[0]);
                list.addElement(ios[0]);

            } else {
                System.out.println("Custom IOS");
                if(numIos > ios.length){
                    numIos = ios.length;
                    ftf_Ios.setText(String.valueOf(ios.length));
                }else if(numIos < ios.length){
                    numIos = ios.length;
                    ftf_Ios.setText(String.valueOf(ios.length));
                }
                
                
                ioIds = new int[numIos];
                int c = 0;
                for (String s : ios) {
                    list.addElement(s);
                    ioIds[c++] = Integer.parseInt(s);
                }
            }
            list_io.setModel(list);
            return true;
        } else {
            return false;
        }

    }

    public boolean checkPos() {
        String xText = tf_Xinc.getText();
        String yText = tf_Yinc.getText();
        String probs = "Problems: ";
        boolean ret = true;

        if (!xText.isEmpty()) {

            String[] x = xText.split(",");
            if (x.length == 1) {
                xInc = new int[1];
                xInc[0] = Integer.parseInt(x[0]);
            } else {
                xInc = new int[x.length];
                for (int i = 0; i < x.length; i++) {
                    xInc[i] = Integer.parseInt(x[i]);
                }
            }

        } else {
            probs += "No X Inc";
            ret = false;
        }

        if (!yText.isEmpty()) {

            String[] y = yText.split(",");
            if (y.length == 1) {
                yInc = new int[1];
                yInc[0] = Integer.parseInt(y[0]);
            } else {
                yInc = new int[y.length];
                for (int i = 0; i < y.length; i++) {
                    yInc[i] = Integer.parseInt(y[i]);
                }
            }

        } else {
            probs += ", No Y Inc";
            ret = false;
        }

        posProblems.setText(probs);

        return ret;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dirGroup = new javax.swing.ButtonGroup();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tf_Output = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tf_Input = new javax.swing.JTextArea();
        panel_Fields = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ftf_Rows = new javax.swing.JFormattedTextField();
        ftf_Cols = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        ftf_Ios = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        ftf_startXpos = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        ftf_startYpos = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tf_ios = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        list_io = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        tf_Xinc = new javax.swing.JTextField();
        tf_Yinc = new javax.swing.JTextField();
        posProblems = new javax.swing.JLabel();
        checkPos = new javax.swing.JButton();
        rb_down = new javax.swing.JRadioButton();
        rb_right = new javax.swing.JRadioButton();
        generate = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jLabel8.setText("jLabel8");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Widget Helper");

        tf_Output.setColumns(20);
        tf_Output.setLineWrap(true);
        tf_Output.setRows(5);
        jScrollPane1.setViewportView(tf_Output);

        tf_Input.setColumns(20);
        tf_Input.setLineWrap(true);
        tf_Input.setRows(5);
        tf_Input.setText("{\n  \"widget_class\": \"Digital%20Value%20-%20Leds\",\n  \"widget_subclass\": \"10\",\n  \"w_x\": \"`%XPOS%`\",\n  \"w_y\": \"`%YPOS%`\",\n  \"station\": \"147\",\n  \"io_id\": \"`%IO_ID%`\",\n    \"code\": \"<div style=\\\"/**main_holder**/padding:5px;\\\">\\n\\t\\n\\t<span id=\\\"holder_1448309198\\\" style=\\\"text-align:center; display:inline-block;\\\"></span>\\n\\t<span style=\\\"/**Label**/padding-left:5px; font-size:14px; font-family:Arial;\\\"><!--*Label*--><!----></span>\\n</div>\\n<script>try {\\ntry {\\ntry {\\n\\n  var server='get_value';\\n  var io_id=`%IO_ID%`;\\n\\tvar val_source=io_id;\\n\\tvar widget_help={\\n\\t\\t\\\"main_help\\\":{\\n\\t\\t\\t\\\"en\\\":\\\"Display a digital input's value.\\\",\\n\\t\\t\\t\\\"ro\\\":\\\"Afișează valoarea unei intrări digitale.\\\"\\n\\t\\t},\\n\\t\\t\\\"OnColor\\\":{\\n\\t\\t\\t\\\"en\\\":\\\"The color of the LED when the input's value is 1. The colour's name should be written here!\\\",\\n\\t\\t\\t\\\"ro\\\":\\\"Culoarea LED-ului când intrarea este pe 1.  Scrieti aici numele culorii!\\\",\\n\\t\\t\\t\\\"width\\\":\\\"60px\\\"\\n\\t\\t},\\n\\t\\t\\\"OffColor\\\":{\\n\\t\\t\\t\\\"en\\\":\\\"The color of the LED when the input's value is 0. The colour's name should be written here!\\\",\\n\\t\\t\\t\\\"ro\\\":\\\"Culoarea LED-ului când intrarea este pe 0. Scrieti aici numele culorii!\\\",\\n\\t\\t\\t\\\"width\\\":\\\"60px\\\"\\n\\t\\t},\\n\\t\\t\\\"ErrColor\\\":{\\n\\t\\t\\t\\\"en\\\":\\\"The color of the LED when there's an error. The colour's name should be written here!\\\",\\n\\t\\t\\t\\\"ro\\\":\\\"Culoarea LED-ului când apare o eroare. Scrieti aici numele culorii!\\\",\\n\\t\\t\\t\\\"width\\\":\\\"60px\\\"\\n\\t\\t},\\n\\t\\t\\\"LEDHeight\\\":{\\n\\t\\t\\t\\\"en\\\":\\\"The LED's height, measured in pixels.\\\",\\n\\t\\t\\t\\\"ro\\\":\\\"Înălțimea LED-ului, măsurată în pixeli.\\\",\\n\\t\\t\\t\\\"width\\\":\\\"30px\\\"\\n\\t\\t},\\n\\t\\t\\\"LEDWidth\\\":{\\n\\t\\t\\t\\\"en\\\":\\\"The LED's width, measured in pixels.\\\",\\n\\t\\t\\t\\\"ro\\\":\\\"Lățimea LED-ului, măsurată în pixeli.\\\",\\n\\t\\t\\t\\\"width\\\":\\\"30px\\\"\\n\\t\\t}\\n\\t};\\n\\n\\tvar editable_params={\\n\\t\\t\\\"OnColor\\\":\\\"green\\\",\\n\\t\\t\\\"OffColor\\\":\\\"grey\\\",\\n\\t\\t\\\"ErrColor\\\":\\\"red\\\",\\n\\t\\t\\\"LEDHeight\\\":20,\\n\\t\\t\\\"LEDWidth\\\":20\\n\\t};\\n  var params={\\n    \\\"color_on\\\":\\\"green\\\",\\n    \\\"color_off\\\":\\\"grey\\\",\\n    \\\"color_err\\\":\\\"red\\\",\\n    \\\"width\\\":10,\\n    \\\"height\\\":10,\\n    \\\"light_width\\\":20,\\n    \\\"light_height\\\":20,\\n\\t\\t\\\"interval\\\":5000};\\n\\tparams.color_on=editable_params.OnColor;\\n\\tparams.color_off=editable_params.OffColor;\\n\\tparams.color_err=editable_params.ErrColor;\\n\\tparams.width=editable_params.LEDWidth;\\n\\tparams.height=editable_params.LEDHeight;\\n\\tparams.light_width=Math.round((editable_params.LEDWidth * 18) / 15);\\n\\tparams.light_height=Math.round((editable_params.LEDHeight * 18) / 15);\\n\\tnew led('holder_1448309198', server, val_source, params);\\n\\n}catch(e){\\n\\talert(e);\\n}\\n\\n}catch(e){\\n\\talert(e);\\n}\\n\\n}catch(e){\\n\\talert(e);\\n}\\n</script>\"\n}");
        jScrollPane2.setViewportView(tf_Input);

        jLabel1.setText("Rows");

        jLabel2.setText("Cols");

        ftf_Rows.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        ftf_Rows.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ftf_Rows.setText("3");

        ftf_Cols.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        ftf_Cols.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ftf_Cols.setText("4");

        jLabel3.setText("Num Ios");

        ftf_Ios.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        ftf_Ios.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ftf_Ios.setText("10");

        jLabel4.setText("Start X Pos");

        ftf_startXpos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        ftf_startXpos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ftf_startXpos.setText("100");

        jLabel5.setText("Start Y Pos");

        ftf_startYpos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        ftf_startYpos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ftf_startYpos.setText("200");

        jLabel6.setText("X Increments");

        jLabel7.setText("Y Increments");

        tf_ios.setColumns(20);
        tf_ios.setLineWrap(true);
        tf_ios.setRows(5);
        tf_ios.setText("10");
        jScrollPane3.setViewportView(tf_ios);

        jScrollPane4.setViewportView(list_io);

        jButton1.setText("List ios");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tf_Xinc.setText("100,85,120");

        tf_Yinc.setText("80,60,120");

        posProblems.setText("Problems:");

        checkPos.setText("Check");
        checkPos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPosActionPerformed(evt);
            }
        });

        dirGroup.add(rb_down);
        rb_down.setSelected(true);
        rb_down.setText("Down then Right");

        dirGroup.add(rb_right);
        rb_right.setText("Right then Down");

        generate.setText("Generate");
        generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_FieldsLayout = new javax.swing.GroupLayout(panel_Fields);
        panel_Fields.setLayout(panel_FieldsLayout);
        panel_FieldsLayout.setHorizontalGroup(
            panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_FieldsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_FieldsLayout.createSequentialGroup()
                        .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_FieldsLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ftf_Ios, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_FieldsLayout.createSequentialGroup()
                                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel_FieldsLayout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ftf_Cols, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel_FieldsLayout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ftf_Rows, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel_FieldsLayout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ftf_startYpos, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel_FieldsLayout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ftf_startXpos, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panel_FieldsLayout.createSequentialGroup()
                                    .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(rb_down, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(rb_right, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGap(86, 86, 86)
                                    .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(generate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(panel_FieldsLayout.createSequentialGroup()
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 21, Short.MAX_VALUE))
                    .addGroup(panel_FieldsLayout.createSequentialGroup()
                        .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(checkPos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_FieldsLayout.createSequentialGroup()
                                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tf_Xinc)
                            .addComponent(tf_Yinc, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(posProblems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panel_FieldsLayout.setVerticalGroup(
            panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_FieldsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftf_startXpos, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftf_Cols, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftf_startYpos, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftf_Rows, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftf_Ios, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_Xinc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_Yinc, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(checkPos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(posProblems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_FieldsLayout.createSequentialGroup()
                        .addComponent(rb_down)
                        .addGap(4, 4, 4)
                        .addComponent(rb_right))
                    .addGroup(panel_FieldsLayout.createSequentialGroup()
                        .addComponent(generate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panel_FieldsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Widget Helper");

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Copy");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_Fields, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(89, 89, 89)))
                        .addGap(26, 26, 26))))
            .addGroup(layout.createSequentialGroup()
                .addGap(392, 392, 392)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1))
                    .addComponent(panel_Fields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        checkIos();


    }//GEN-LAST:event_jButton1ActionPerformed


    private void checkPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPosActionPerformed
        // TODO add your handling code here:

        update();
    }//GEN-LAST:event_checkPosActionPerformed

    private void generateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateActionPerformed
        // TODO add your handling code here:
        
        update();
        

        if (gen) {

            String output = "[";
            String text = tf_Input.getText();

            for (int i = 0; i < ioIds.length; i++) {
                String[] info = listString[i].split(",");

                String newString = text.replace("`%IO_ID%`", info[0])
                        .replace("`%XPOS%`", info[1])
                        .replace("`%YPOS%`", info[2]);

                output += newString;
                if (i != (ioIds.length - 1)) {
                    output += ",";
                }
            }

            output += "]";
            tf_Output.setText(output);

            Highlighter h = tf_Output.getHighlighter();
            h.removeAllHighlights();
            int sel = tf_Output.getText().length();
            if (sel > 0) {
                try {
                    h.addHighlight(0, sel, DefaultHighlighter.DefaultPainter);
                } catch (BadLocationException ex) {
                    System.out.println("Bad selection");
                }
            }
            StringSelection stringSelection = new StringSelection(tf_Output.getText());
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
        } else {
            posProblems.setText("Fix before generating again");
        }
    }//GEN-LAST:event_generateActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        Highlighter h = tf_Output.getHighlighter();
        h.removeAllHighlights();
        tf_Output.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        Highlighter h = tf_Output.getHighlighter();
            h.removeAllHighlights();
            int sel = tf_Output.getText().length();
            if (sel > 0) {
                try {
                    h.addHighlight(0, sel, DefaultHighlighter.DefaultPainter);
                } catch (BadLocationException ex) {
                    System.out.println("Bad selection");
                }
            }
            StringSelection stringSelection = new StringSelection(tf_Output.getText());
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkPos;
    private javax.swing.ButtonGroup dirGroup;
    private javax.swing.JFormattedTextField ftf_Cols;
    private javax.swing.JFormattedTextField ftf_Ios;
    private javax.swing.JFormattedTextField ftf_Rows;
    private javax.swing.JFormattedTextField ftf_startXpos;
    private javax.swing.JFormattedTextField ftf_startYpos;
    private javax.swing.JButton generate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList list_io;
    private javax.swing.JPanel panel_Fields;
    private javax.swing.JLabel posProblems;
    private javax.swing.JRadioButton rb_down;
    private javax.swing.JRadioButton rb_right;
    private javax.swing.JTextArea tf_Input;
    private javax.swing.JTextArea tf_Output;
    private javax.swing.JTextField tf_Xinc;
    private javax.swing.JTextField tf_Yinc;
    private javax.swing.JTextArea tf_ios;
    // End of variables declaration//GEN-END:variables
}

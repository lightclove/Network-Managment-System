package ceeport.netManage.gui;

import ceeport.netManage.device.IDevice;
import ceeport.netManage.device.impl.AccessPoint;
import ceeport.netManage.device.impl.PhoneServer;
import ceeport.netManage.device.impl.phone.Phone;
import ceeport.netManage.device.impl.phone.PhoneClient;
import ceeport.netManage.device.state.DeviceState;
import ceeport.netManage.snmp.SnmpHandler;
import ceeport.netManage.util.BsoriLogger;
import ceeport.tools.OS_Info;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.activities.PTransformActivity;
import edu.umd.cs.piccolo.event.*;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDimension;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.pswing.PSwing;
import edu.umd.cs.piccolox.pswing.PSwingCanvas;
import edu.umd.cs.piccolox.swing.PScrollPane;
//import org.openqa.selenium.Alert;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The main application's frame.
 */
public class MainFrame extends PFrame {

    final static BsoriLogger LOGGER = BsoriLogger.getLogger(MainFrame.class);

    private static final long serialVersionUID = 1L;

    /**
     * The length of the vertical leg between a device and a connecting line.
     */
    private static final int VERTICAL_LEG_BOTTOM = -60;

    private static final int VERTICAL_LEG_TOP = 60;

    private MainData data;
    private PText tooltipNode;
    private PText statTitle;
    private PText phoneClientsTableTitle;
    private IDevice selectedDevice;
    private Phone selectedPhone;
    private JTable statTable;
    private JTable phoneClientsTable;
    private PLayer nodeLayer;
    private PLayer edgeLayer;
    private PActivity changeRandomNodeStateActivity;
    private CustomZoomHandler customZoomHandler;
    private boolean autoZoom = false;

    private PSwingCanvas canvas0;
    private PSwingCanvas canvas1;
    private PSwingCanvas buildStatTableCanvas;
    private PSwingCanvas canvas3;
    private PSwingCanvas canvas4;

    private PSwingCanvas phoneClientsCanvas;
    JSplitPane verticalSplitPaneForTables;

    public MainFrame(MainData data) {
        super("Интерфейс МДА БСОРИ ", false, new PSwingCanvas());
//        Image img = new ImageIcon(Launcher.class.getResource("./img/AME_icon.ico")).getImage();
//        this.setIconImage(img); 
        this.data = data;
        setSize(1920, 1080);
        setLocation(0, 0);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                Object[] options = {"Да", "Нет"};
                int result = JOptionPane.showOptionDialog(new JFrame(),
                        "Вы действительно хотите выйти из программы ?", "Подтверждение выхода",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        options, options[1]);

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        Launcher.stopApp();
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                } else {
                    // Do nothing
                }
            }
        });
//        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
//        ses.scheduleAtFixedRate(new Runnable() {
//            PSwingCanvas[] ps = new PSwingCanvas[]{canvas0, canvas1, canvas2};
//
//            @Override
//            public void run() {
//                updateGraphics(ps);
//            }
//        }, 0, 1, TimeUnit.SECONDS);
    }


    public void initialize() {
        try {
            createCanvas();
            showInitDialog(15000); //150 sec
            //splashScreen(5000);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    // Drawing frame and text label for BSKP 
    private PNode drawShape(int[] x, int[] y, String textLabel) {

        Polygon p = new Polygon(x, y, x.length);
        PNode n = new PPath(p);
        PText pTextLabel = new PText(textLabel);
        pTextLabel.setX(n.getX() + 45);
        pTextLabel.setY(n.getY());

        n.addChild(pTextLabel);
        return n;
    }

    private void createCanvas() throws IOException {

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        canvas0 = buildLabelTitleCanvas("");
        canvas0.setPreferredSize(new Dimension(50, 50));
        canvas0.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        container.add(canvas0, BorderLayout.NORTH);

        canvas1 = buildMainCanvas("");
        canvas1.setPreferredSize(new Dimension(800, 800));
        canvas1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        PScrollPane scrollPane1 = new PScrollPane(canvas1);
        //container.add(scrollPane1, BorderLayout.CENTER);

        buildStatTableCanvas = buildStatTableCanvas("");
        buildStatTableCanvas.setPreferredSize(new Dimension(1000, 500));
        buildStatTableCanvas.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        PScrollPane buildStatTableScrollPane = new PScrollPane(buildStatTableCanvas);
        //scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //container.add(scrollPane2, BorderLayout.NORTH);

        phoneClientsCanvas = buildPhoneClientsTableCanvas("");
        phoneClientsCanvas.setPreferredSize(new Dimension(1000, 800));
        phoneClientsCanvas.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        PScrollPane phoneClientsScrollPane = new PScrollPane(phoneClientsCanvas);

        verticalSplitPaneForTables = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                buildStatTableScrollPane, phoneClientsScrollPane);
        verticalSplitPaneForTables.setOneTouchExpandable(true);
        verticalSplitPaneForTables.setDividerLocation(800);
        verticalSplitPaneForTables.setDividerSize(20);
        container.add(verticalSplitPaneForTables, BorderLayout.CENTER);

        final JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                scrollPane1, verticalSplitPaneForTables);
        horizontalSplitPane.setOneTouchExpandable(true);
        horizontalSplitPane.setDividerLocation(930);
        horizontalSplitPane.setDividerSize(20);


        canvas4 = buildButtonsCanvas("");
        canvas4.setPreferredSize(new Dimension(1000, 250));
        canvas4.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        //container.add(canvas4, BorderLayout.SOUTH);
        //createZoomButton(canvas4, canvas1 );
        createZoomSlider(canvas4, canvas1, "Масштаб в процентах");
        PScrollPane scrollPane4 = new PScrollPane(canvas4);

        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                horizontalSplitPane, scrollPane4);
        verticalSplitPane.setOneTouchExpandable(true);
        verticalSplitPane.setDividerLocation(800);
        verticalSplitPane.setDividerSize(20);
        container.add(verticalSplitPane, BorderLayout.CENTER);
    }


    private PSwingCanvas buildMainCanvas(String canvasName) {
        final PSwingCanvas canvas = new PSwingCanvas();
        createMenu();
        createNodesAndEdges(canvas);
        return canvas;
    }

    private PSwingCanvas buildButtonsCanvas(String canvasName) {
        PSwingCanvas canvas = new PSwingCanvas();
        canvas.getLayer().addChild(new PSwing(new JLabel(canvasName)));
        canvas.setPanEventHandler(null);
        return createButtons(canvas);
    }

    private PSwingCanvas buildLabelTitleCanvas(String canvasName) {
        PSwingCanvas canvas = new PSwingCanvas();
        canvas.getLayer().addChild(new PSwing(new JLabel(canvasName)));
        canvas.setPanEventHandler(null);
        return createLabel(canvas, "Схема сетевой инфраструктуры", 10, 10, 30);
    }

    private PSwingCanvas createLabel(PSwingCanvas pSwingCanvas, String text, int x, int y, int fontSize) {
        PText title = new PText(text);
        title.setFont(new java.awt.Font("SansSerif", 3, fontSize));
        title.setX(x);
        title.setX(y);
        pSwingCanvas.getLayer().addChild(title);
        return pSwingCanvas;
    }

    /**
     * Creates and lays out nodes
     */
    private PSwingCanvas createNodesAndEdges(PSwingCanvas pSwingCanvas) {

        nodeLayer = getCanvas().getLayer();
        edgeLayer = new PLayer();

        pSwingCanvas.getCamera().addLayer(edgeLayer);
        pSwingCanvas.getCamera().addLayer(nodeLayer);

        PText title = new PText("");
        title.setFont(new java.awt.Font("SansSerif", 3, 24));
        title.setX(100);
        nodeLayer.addChild(title);

        // create device images
        List<PDeviceImage> deviceImages = new ArrayList<PDeviceImage>();
        for (IDevice device : data.getAllDevices()) {
            PDeviceImage deviceImage = new PDeviceImage(device);
            deviceImages.add(deviceImage);
            nodeLayer.addChild(deviceImage);

            if (device instanceof AccessPoint) {
                // for access points create their phone images too
                AccessPoint accessPoint = (AccessPoint) device;
                Phone phone = accessPoint.getPhone();
                phone.getPhoneImage().setEdgeLayer(edgeLayer);
                PImage phoneImage = phone.getPhoneImage();
                nodeLayer.addChild(phoneImage);
            }
        }

        // Отрисовка прямоугольника с метками
        //nodeLayer.addChild(drawShape(new int[]{data.findDeviceById(51).getX() - 10, data.findDeviceById(51).getX() - 10, data.findDeviceById(51).getX() + 110, data.findDeviceById(51).getX() + 110}, new int[]{data.findDeviceById(51).getY() + 150, data.findDeviceById(51).getY() - 10, data.findDeviceById(51).getY() - 10, data.findDeviceById(51).getY() + 150}, "БСК"));

        // connect devices with lines (edges)
        for (IDevice device : data.getAllDevices()) {
            drawEdgesFromDeviceToChildren(device);
        }

        // Create event handler to drag and drop devices
        nodeLayer.addInputEventListener(new NodeDragHandler());
        // add right click menu hanlder
        pSwingCanvas.addInputEventListener(new CanvasInputHandler());
        // Move (pan) screen with Ctrl key
        pSwingCanvas.getPanEventHandler().setEventFilter(new PInputEventFilter(InputEvent.CTRL_MASK));

//        customZoomHandler = new CustomZoomHandler();
//        customZoomHandler.setEventFilter(new PInputEventFilter(InputEvent.SHIFT_MASK));

        pSwingCanvas.setZoomEventHandler(customZoomHandler);

        createUpdateScreenActivity();

        createPollingActivity();

        createToolTip(pSwingCanvas);

        return pSwingCanvas;
    }

    private PSwingCanvas createZoomSlider(final PSwingCanvas pSwingCanvasToAdd, final PSwingCanvas pSwingCanvasZoomable, String label) {

        //Create the label.
        JLabel sliderLabel = new JLabel("Масштаб в процентах", JLabel.CENTER);

        JSlider zoomSlider = new JSlider(50, 150);

        class SliderListener implements ChangeListener {

            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int sliderValue = (int) source.getValue();

                    switch (sliderValue) {
                        case 50:
                            pSwingCanvasZoomable.getCamera().setViewScale(0.5);
                            pSwingCanvasZoomable.getCamera().setViewOffset(0, 0);
                            //JOptionPane.showMessageDialog(null, "Удерживайте клавишу \"Ctrl\" и перетаскивайте рисунок мышью для навигации.");
                            break;
                        case 100:
                            pSwingCanvasZoomable.getCamera().setViewScale(1.0);
                            pSwingCanvasZoomable.getCamera().setViewOffset(0, 0);
                            break;
                        case 150:
                            pSwingCanvasZoomable.getCamera().setViewScale(1.5);
                            pSwingCanvasZoomable.getCamera().setViewOffset(0, 0);
                            break;
                    }
                }
            }
        }
        zoomSlider.addChangeListener(new SliderListener());
        zoomSlider.setMajorTickSpacing(50);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.setSnapToTicks(true);

        PSwing pSwing = new PSwing(zoomSlider);
        pSwing.translate(310, 20);
        pSwingCanvasToAdd.getLayer().addChild(pSwing);

        PSwing pSwing2 = new PSwing(sliderLabel);
        pSwingCanvasToAdd.getLayer().addChild(pSwing2);
        pSwing2.translate(350, 0);

        return pSwingCanvasZoomable;
    }

    private void createUpdateScreenActivity() {
        PActivity updateScreenActivity = new PActivity(-1, IDevice.SCREEN_UPDATE_INTERVAL) {
            protected void activityStep(long time) {
                super.activityStep(time);
                try {
                    // logger.log(Level.INFO, "update screen activity called...");
                    setTableData();
                    setPhoneClientsTableData();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
                if (selectedDevice != null) {
                    selectDevice(selectedDevice.getDeviceImage());
                }
                try {
                    setPhoneClientsTableData();
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        nodeLayer.addActivity(updateScreenActivity);
    }

    private void createPollingActivity() {
        PActivity pollDeviceActivity = new PActivity(-1, IDevice.DEVICE_POLL_INTERVAL) {
            protected void activityStep(long time) {
                super.activityStep(time);
                //logger.log(Level.INFO, "poll all devices...");
                System.out.println("Опрос всех устройств по snmp...");
                System.out.println();
                try {
                    SnmpHandler.getInstance().pollAllDevices();

                } catch (Throwable ex) {
                    System.out.println(ex);
                }
            }
        };
        nodeLayer.addActivity(pollDeviceActivity);
    }

    /**
     * Adds buttons to the screen
     */
    private PSwingCanvas createButtons(final PSwingCanvas pSwingCanvas) {

        PSwing pSwing;
        JButton reInitProgramm = new JButton("<html><font size=7 color=red >Диагностика</font></html>");
        reInitProgramm.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    Launcher.stopApp();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        });
        pSwing = new PSwing(reInitProgramm);
        pSwing.translate(780, 70);
        pSwingCanvas.getLayer().addChild(pSwing);
        pSwingCanvas.getLayer().addChild(pSwing);
        pSwingCanvas.getLayer().addChild(pSwing);
        //pSwing.repaint ();
        return pSwingCanvas;

    }

    private PSwingCanvas createStatTable(PSwingCanvas pSwingCanvas) throws IOException {
        statTable = new JTable();
        JScrollPane pane = new JScrollPane(statTable);
        pane.setPreferredSize(new Dimension(878, 740));
        statTable.setDoubleBuffered(false);
        //statTable.setAutoCreateRowSorter(true);
        statTitle = new PText("Общая статистика МДА БСОРИ:");
        statTitle.setFont(new java.awt.Font("SansSerif", 3, 20));
        statTitle.translate(250, 20);
        pSwingCanvas.getLayer().addChild(statTitle);
        PSwing pSwing = new PSwing(pane);
        pSwing.translate(60, 50);
        pSwingCanvas.getLayer().addChild(pSwing);
        pSwing.repaint();
        return pSwingCanvas;
    }


    private void setTableData() throws IOException {
        Properties p1 = new Properties();
        Properties p2 = new Properties();
        p1.load(new InputStreamReader(new FileInputStream("./placement.properties"), "UTF-8"));
        p2.load(new InputStreamReader(new FileInputStream("./zip.properties"), "UTF-8"));
        final Vector<String> columns = new Vector<String>();
        columns.add("№");
        columns.add("Имя устр.");
        columns.add("IP адрес");
        columns.add("Местоположение устройства");//columns.add("Серийный номер");
        columns.add("Состояние");
        //columns.add("Время работы");
        columns.add("Расположение ЗИП");

        Vector<Vector> rows = new Vector<Vector>();
        for (IDevice device : data.getAllDevices()) {
            Vector row = new Vector();
            row.add(device.getId());
            row.add(device.getViewName());
            row.add(device.getIpAddress());
            row.add(p1.getProperty(device.getName()));
            row.add(device.getState());

            //row.add(device.getSysUpTime());
            row.add(p2.getProperty(device.getName()));
            rows.add(row);
        }

        statTable.setModel(new DefaultTableModel(rows, columns) {
            Class[] types = {
                    Integer.class,
                    String.class,
                    String.class,
                    String.class,
                    Integer.class,
                    String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
        });

        //set cell render for '№' column
        statTable.getColumnModel().getColumn(0).setCellRenderer(
                new DefaultTableCellRenderer() {
                    protected void setValue(Object value) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setVerticalAlignment(SwingConstants.CENTER);
                        setText(
                                (value == null)
                                        ? ""
                                        : String.valueOf(value)
                        );
                    }
                }
        );
        //set cell render for Имя устр.' column
        statTable.getColumnModel().getColumn(1).setCellRenderer(
                new DefaultTableCellRenderer() {
                    protected void setValue(Object value) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setVerticalAlignment(SwingConstants.CENTER);
                        setText(
                                (value == null)
                                        ? ""
                                        : String.valueOf(value)
                        );
                    }
                }
        );
        //set cell render for 'IP адрес' column
        statTable.getColumnModel().getColumn(2).setCellRenderer(
                new DefaultTableCellRenderer() {
                    protected void setValue(Object value) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setVerticalAlignment(SwingConstants.CENTER);
                        setText(
                                (value == null)
                                        ? ""
                                        : String.valueOf(value)
                        );
                    }
                }
        );

        // set cell render for 'Местоположение устройства' column
        statTable.getColumnModel().getColumn(3).setCellRenderer(
                new DefaultTableCellRenderer() {
                    protected void setValue(Object value) {
                        String ns = "";
                        String[] s = String.valueOf(value).split("\\s+");

                        for (int i = 0; i < s.length; i++) {
                            ns += s[i] + " ";

                        }
                        if (ns.length() > 25) statTable.setRowHeight(40);
                        setText(
                                (value == null)
                                        ? ""
                                        : String.valueOf("<html>" + ns + "</html>")
                        );
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setVerticalAlignment(SwingConstants.CENTER);
                    }
                }
        );

        // set cell render for 'Состояние' column
        statTable.getColumnModel().getColumn(4).setCellRenderer(
                new DefaultTableCellRenderer() {
                    protected void setValue(Object value) {
                        if (value == null) {
                            setText("");
                        } else {
                            setHorizontalAlignment(SwingConstants.CENTER);
                            setVerticalAlignment(SwingConstants.CENTER);
                            DeviceState deviceState = (DeviceState) value;
                            if (deviceState == DeviceState.ON) {
                                setText("ВКЛ.");
                                setBackground(Color.green);
                            } else if (deviceState == DeviceState.OFF) {
                                setText("ОТКЛ.");
                                setBackground(Color.RED);

                            }
                        }
                    }
                }
        );

        // set cell render for 'Расположение ЗИП' column
        statTable.getColumnModel().getColumn(5).setCellRenderer(
                new DefaultTableCellRenderer() {
                    protected void setValue(Object value) {
                        String ns = "";
                        String[] s = String.valueOf(value).split("\\s+");

                        for (int i = 0; i < s.length; i++) {
                            ns += s[i] + " ";

                        }
                        if (ns.length() > 25) statTable.setRowHeight(40);
                        setText(
                                (value == null)
                                        ? ""
                                        : String.valueOf("<html>" + ns + "</html>")
                        );
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setVerticalAlignment(SwingConstants.CENTER);
                    }
                }
        );


        statTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = statTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedRow = statTable.convertRowIndexToModel(selectedRow);
                        Integer deviceId = (Integer) statTable.getModel().getValueAt(selectedRow, 0);
                        IDevice device = data.findDeviceById(deviceId);
                        selectDeviceNode(device.getDeviceImage());

                        if (device instanceof AccessPoint) {
                            AccessPoint accessPoint = (AccessPoint) device;
                            try {
                                selectPhoneNode(accessPoint.getPhone().getPhoneImage());
                            } catch (IOException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                }
            }
        });

        statTable.setAutoCreateRowSorter(true);
        statTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //statTable.setRowHeight(50);

        TableColumn c = statTable.getColumn(statTable.getColumnName(0));
        c.setPreferredWidth(30);
        c = statTable.getColumn(statTable.getColumnName(1));
        c.setPreferredWidth(90);
        c = statTable.getColumn(statTable.getColumnName(2));
        c.setPreferredWidth(100);

        c = statTable.getColumn(statTable.getColumnName(3));
        c.setPreferredWidth(280);
        c = statTable.getColumn(statTable.getColumnName(4));
        c.setPreferredWidth(80);
        c = statTable.getColumn(statTable.getColumnName(5));
        c.setPreferredWidth(280);
    }

    private PSwingCanvas createPhoneClientsTable(PSwingCanvas pSwingCanvas) throws IOException {
        phoneClientsTable = new JTable();
        JScrollPane pane = new JScrollPane(phoneClientsTable);
        pane.setPreferredSize(new Dimension(500, 340));
        phoneClientsTable.setDoubleBuffered(false);
        phoneClientsTableTitle = new PText("Абоненты ББПИ:");
        phoneClientsTableTitle.setFont(new java.awt.Font("SansSerif", 3, 20));
        phoneClientsTableTitle.translate(250, 20);
        pSwingCanvas.getLayer().addChild(phoneClientsTableTitle);
        PSwing pSwing = new PSwing(pane);
        pSwing.translate(60, 50);
        pSwingCanvas.getLayer().addChild(pSwing);
        pSwing.repaint();
        return pSwingCanvas;
    }

    public void setPhoneClientsTableData() throws FileNotFoundException, IOException {

        if (selectedPhone == null) {
            return;
        }

        // set table's title
        phoneClientsTableTitle.setText("Абоненты точки доступа:" + selectedPhone.getAccessPoint().getViewName());

        final Vector<String> columns = new Vector<String>();
        columns.add("№");
        columns.add("Имя абонента");
        //columns.add("IP адрес");
        columns.add("MAC адрес");

        Vector<Vector<Object>> rows = new Vector<>();

        Properties p = new Properties();
        p.load(new InputStreamReader(new FileInputStream("./mpt.properties"), "UTF-8"));
        int z = 0; //Number counter
        for (PhoneClient phoneClient : selectedPhone.getPhoneClients()) {
            z++;
            Vector<Object> row = new Vector<>();
            //row.add(phoneClient.getId());
            row.add(z);
            //row.add(phoneClient.getName());
            row.add(p.getProperty(phoneClient.getMac()));
            //row.add(phoneClient.getIp());
            row.add(phoneClient.getMac());
            rows.add(row);
        }

        phoneClientsTable.setModel(new DefaultTableModel(rows, columns) {
            Class<?>[] types = {
                    Integer.class,
                    String.class,
                    //String.class,
                    String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
        });

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            protected void setValue(Object value) {
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
                setText(
                        (value == null)
                                ? ""
                                : String.valueOf(value)
                );
            }
        };

        for (int i = 0; i < phoneClientsTable.getColumnModel().getColumnCount(); i++) {
            phoneClientsTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        phoneClientsTable.setAutoCreateRowSorter(true);
        phoneClientsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //phoneClientsTable.setRowHeight(50);

        TableColumn c = phoneClientsTable.getColumn(phoneClientsTable.getColumnName(0));
        c.setPreferredWidth(50); // id

        c = phoneClientsTable.getColumn(phoneClientsTable.getColumnName(1));
        c.setPreferredWidth(200); // name

//        c = phoneClientsTable.getColumn(phoneClientsTable.getColumnName(2));
//        c.setPreferredWidth(100); // ip

        c = phoneClientsTable.getColumn(phoneClientsTable.getColumnName(2));
        c.setPreferredWidth(130); // mac
    }


    /**
     * Creates a tooltip
     */
    private void createToolTip(PSwingCanvas pSwingCanvas) {
        final PCamera camera = pSwingCanvas.getCamera();
        tooltipNode = new PText();
        tooltipNode.setPaint(new Color(255, 252, 209));
        tooltipNode.setPickable(false);
        camera.addChild(tooltipNode);
        camera.addInputEventListener(new PBasicInputEventHandler() {
            public void mouseMoved(final PInputEvent event) {
                try {
                    updateToolTip(event);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }

            public void mouseDragged(final PInputEvent event) {
                try {
                    updateToolTip(event);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }

            public void updateToolTip(final PInputEvent event) throws IOException {

                Properties p1 = new Properties();
                Properties p2 = new Properties();
                p1.load(new InputStreamReader(new FileInputStream("./placement.properties"), "UTF-8"));
                p2.load(new InputStreamReader(new FileInputStream("./zip.properties"), "UTF-8"));

                PNode node = event.getPickedNode();

                if (!(node instanceof PDeviceImage) && !(node instanceof PPhoneImage)) {
                    tooltipNode.setVisible(false);
                    return;
                }

                IDevice device = null;
                String tooltipString = null;

                if (node instanceof PPhoneImage) {
                    PPhoneImage phoneImage = (PPhoneImage) node;
                    tooltipString = createPhoneTooltipText(phoneImage.getPhone());
                } else {
                    device = ((PDeviceImage) node).getDevice();
                    if (device == null) {
                        return;
                    }
                }

                tooltipNode.setVisible(true);


                if (device instanceof AccessPoint) {
                    tooltipString = String.format(
                            "Имя устройства: %s%n"
                                    + "IP адрес устройства: %s%n"
                                    + "Тех. состояние устройства: %s%n"
                                    + "Местоположение устройства: %s%n"
                                    + "Расположение ЗИП: %s%n",
                            device.getName(),
                            device.getIpAddress(),
                            device.getState().toString().replace("O", "В").replace("N", "КЛ").replace("FF", "ЫКЛ"),
                            p1.getProperty(device.getName()),
                            p2.getProperty(device.getName())
                            //device.getClientsMac()
                    );
                } else if (device instanceof IDevice) {
                    tooltipString = String.format(
                            "Имя устройства: %s%n"
                                    + "IP адрес устройства: %s%n"
                                    + "Тех.состояние устройства: %s%n"
                                    + "Местоположение устройства: %s%n"
                                    + "Расположение ЗИП: %s%n",
                            device.getName(),
                            device.getIpAddress(),
                            device.getState().toString().replace("O", "В").replace("N", "КЛ").replace("FF", "ЫКЛ"),
                            p1.getProperty(device.getName()),
                            p2.getProperty(device.getName())
                    );
                }

                tooltipNode.setText(tooltipString);

                calculateTooltipPosition(camera, event);
            }

            private void calculateTooltipPosition(final PCamera camera, final PInputEvent event) {
                // calculate whether the tooltip goes beyound the visible area
                // if so, position it so that it's visible again

                Point2D p = event.getCanvasPosition();
                event.getPath().canvasToLocal(p, camera);

                double tW = tooltipNode.getWidth();
                double tH = tooltipNode.getHeight();

                double fW = canvas1.getWidth();
                double fH = canvas1.getHeight();

                double mX = event.getCanvasPosition().getX() + 8;
                double mY = event.getCanvasPosition().getY() - 8;

                if ((mY + tH) > fH) {
                    mY = mY - (mY + tH - fH);
                }

                if ((mX + tW) > fW) {
                    mX = mX - (mX + tW - fW);
                }

                tooltipNode.setOffset(mX, mY);
            }
        });
    }

    private String createPhoneTooltipText(Phone phone) {
        if (!phone.isShown()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Абоненты ББПИ: ");
        sb.append(phone.getAccessPoint().getViewName());
        sb.append('\n');
        sb.append('\n');

        for (PhoneClient client : phone.getPhoneClients()) {
            sb.append(client.getName());
            sb.append('\n');
        }

        return sb.toString();
    }

    private void drawEdgesFromDeviceToChildren(IDevice device) {
        if (device.getChildren() != null) {
            for (IDevice child : device.getChildren()) {
                drawEdgesFromParentToChild(device, child);
            }
        }
    }

    private void drawEdgesFromParentToChild(IDevice parent, IDevice child) {

        if (parent == null || child == null) {
            return;
        }

        PDeviceImage parentDeviceImage = parent.getDeviceImage();
        PDeviceImage childDeviceImage = child.getDeviceImage();
        Point2D.Double parentBounds;
        Point2D.Double childBounds;

        PPath edge = childDeviceImage.getEdge();
        if (edge != null) {
            edge.reset();
            parentBounds = (Point2D.Double) parentDeviceImage.getFullBounds().getCenter2D();
            childBounds = (Point2D.Double) childDeviceImage.getFullBounds().getCenter2D();
        } else {
            parentBounds = (Point2D.Double) parentDeviceImage.getBounds().getCenter2D();
            childBounds = (Point2D.Double) childDeviceImage.getBounds().getCenter2D();
            edge = new PPath();
            childDeviceImage.setEdge(edge);
            edgeLayer.addChild(edge);
        }

        edge.moveTo((float) parentBounds.getX(), (float) parentBounds.getY());
        int offset = 0;
        // draw legs for access points
        if (parentBounds.getY() < childBounds.getY()) {
            offset = VERTICAL_LEG_BOTTOM;
        } else {
            offset = VERTICAL_LEG_TOP;
        }

        edge.lineTo((float) childBounds.getX(), (float) childBounds.getY() + offset);
        edge.lineTo((float) childBounds.getX(), (float) childBounds.getY());

    }

    private PSwingCanvas buildStatTableCanvas(String canvasName) throws IOException {

        PSwingCanvas canvas = new PSwingCanvas();
        canvas.getLayer().addChild(new PSwing(new JLabel(canvasName)));
        canvas.setPanEventHandler(null);

        return createStatTable(canvas);
    }

    private PSwingCanvas buildPhoneClientsTableCanvas(String canvasName) throws IOException {

        PSwingCanvas canvas = new PSwingCanvas();
        canvas.getLayer().addChild(new PSwing(new JLabel(canvasName)));
        canvas.setPanEventHandler(null);

        return createPhoneClientsTable(canvas);
    }


    private void showInitDialog(int timeActiveInMillis) { // Показывает диалог на время инициализации


        class UserDefTimerTask extends TimerTask {
            public JFrame jf;

            public UserDefTimerTask(JFrame jf) {
                this.jf = jf;
            }

            @Override
            public void run() {
                if (jf.isShowing()) {
                    jf.setVisible(false);
                    jf.dispose();
                }
            }

        }

        JFrame initDialog = new JFrame("Инициализация");
        initDialog.getContentPane().setBackground(Color.lightGray);
        initDialog.setBounds(300, 400, 350, 50);
        JLabel initLabel = new JLabel("Подождите, идет инициализация приложения... ");
        initLabel.setHorizontalAlignment(SwingConstants.CENTER);
        initDialog.add(initLabel);
        initDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        initDialog.setAlwaysOnTop(true);
        initDialog.setUndecorated(true);
        initDialog.setVisible(true);

        UserDefTimerTask t = new UserDefTimerTask(initDialog);

        Timer timer = new Timer();
        timer.schedule(t, timeActiveInMillis);


    }

    class CanvasInputHandler extends PBasicInputEventHandler {

        public void mouseReleased(final PInputEvent e) {
            if (!(e.getPickedNode() instanceof PDeviceImage)) {
                if (e.isRightMouseButton()) {
                    e.setHandled(true);
                    Point2D ePos = e.getCanvasPosition();
                    //ServiceRightClickMenu menu = new ServiceRightClickMenu();
                    //menu.show(getCanvas(), (int) ePos.getX(), (int) ePos.getY());
                }
            }
        }
    }

    class NodeDragHandler extends PDragSequenceEventHandler {

        public NodeDragHandler() {
            //getEventFilter().setMarksAcceptedEventsAsHandled(true);
        }

        public void mouseEntered(final PInputEvent e) {
        }

        public void mouseExited(final PInputEvent e) {
            if (e.getButton() == 0) {

                //e.getPickedNode().setPaint(Color.white);
            }
        }

        public void mousePressed(final PInputEvent event) {
            super.mousePressed(event);
            setIsDragging(false);

        }

        public void mouseReleased(final PInputEvent e) {

            if (isDragging()) {
                endDrag(e);
            } else {
                PNode pickedNode = e.getPickedNode();

                doZoomInOut(pickedNode);

                if (pickedNode instanceof PDeviceImage) {
                    selectDevice((PDeviceImage) pickedNode);

                    PDeviceImage pDeviceImage = (PDeviceImage) pickedNode;
                    IDevice device = pDeviceImage.getDevice();
                    if (device instanceof AccessPoint) {
                        AccessPoint accessPoint = (AccessPoint) device;
                        try {
                            selectPhoneNode(accessPoint.getPhone().getPhoneImage());
                        } catch (IOException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    // Вызов всплывающего меню через двойной клик мыши мыши - адаптировано для планшетного режима
                    if (e.getClickCount() == 1) {
                        e.setHandled(true);
                        Point2D ePos = e.getCanvasPosition();
                        DeviceLeftClickMenu menu = new DeviceLeftClickMenu();
                        menu.show(getCanvas(), (int) ePos.getX(), (int) ePos.getY());
                    }

                } else if (pickedNode instanceof PPhoneImage) {
                    PPhoneImage phoneImage = (PPhoneImage) pickedNode;
                    try {
                        selectPhoneNode(phoneImage);
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    selectDevice(phoneImage.getAccessPoint().getDeviceImage());
                }

// Вызов всплывающего меню через правую кнопку мыши
//                if (e.isRightMouseButton()) {
//                    e.setHandled(true);
//                    Point2D ePos = e.getCanvasPosition();
//                    DeviceRightClickMenu menu = new DeviceRightClickMenu();
//                    menu.show(getCanvas(), (int) ePos.getX(), (int) ePos.getY());
//                }

            }
        }
// 12.05.14 13:30 #F256 : Prohibit dragging devices
//        public void drag(final PInputEvent e) {
//            super.drag(e);
//
//            if (!(e.getPickedNode() instanceof PDeviceImage)) {
//                return;
//            }
//
//            PDeviceImage deviceImage = (PDeviceImage) e.getPickedNode();
//            deviceImage.translate(e.getDelta().width, e.getDelta().height);
//
//            IDevice device = deviceImage.getDevice();
//            // draw edges from this device's parent to this device
//            drawEdgesFromParentToChild(device.getParent(), device);
//
//            // draw edges from node to its children			
//            drawEdgesFromDeviceToChildren(device);
//        }
    }

    private void selectDeviceNode(PDeviceImage newDevice) {
        // reset previous selection
        if (selectedDevice != null) {
            selectedDevice.getDeviceImage().getDeviceName().setPaint(Color.white);
        }

        selectedDevice = newDevice.getDevice();
        selectedDevice.getDeviceImage().getDeviceName().setPaint(Color.orange);
    }

    private void selectDevice(PDeviceImage pickedNode) {
        selectDeviceNode(pickedNode);
        int selectedRow = returnRowIndexForValue(selectedDevice.getId(), 0, statTable);
        statTable.setRowSelectionInterval(selectedRow, selectedRow);
    }

    private void selectPhoneNode(PPhoneImage newPhone) throws IOException {
        // reset previous selection
        if (selectedPhone != null) {
            selectedPhone.getPhoneImage().getNumberOfClients().setPaint(Color.white);
        }

        selectedPhone = newPhone.getPhone();

        selectedPhone.getPhoneImage().getNumberOfClients().setPaint(Color.orange);
        setPhoneClientsTableData();

        int splitterLocation = verticalSplitPaneForTables.getDividerLocation();
        if (splitterLocation <= 800 && splitterLocation >= 750) {
            verticalSplitPaneForTables.setDividerLocation(400);
        }

    }

    private int returnRowIndexForValue(Object value, int columnNumber, JTable table) {
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, columnNumber).equals(value)) {
                return i;
            }
        }
        return -1;
    }

    private void doZoomInOut(PNode deviceImage) {
        if (!autoZoom) {
            return;
        }

        PActivity panActivity = animateViewToCenterBounds(getCanvas().getCamera(), deviceImage.getFullBounds(), 2, 500);
        panActivity.setStartTime(new Date().getTime());
    }

    public PTransformActivity animateViewToCenterBounds(PCamera camera, final Rectangle2D centerBounds, double scale,
                                                        final long duration) {
        final PBounds viewBounds = camera.getViewBounds();
        final PDimension delta = viewBounds.deltaRequiredToCenter(centerBounds);
        final PAffineTransform newTransform = camera.getViewTransform();
        newTransform.translate(delta.width, delta.height);

        if (scale != Double.POSITIVE_INFINITY && scale != 0) {
            newTransform.scaleAboutPoint(scale, centerBounds.getCenterX(), centerBounds.getCenterY());
        }

        return camera.animateViewToTransform(newTransform, duration);
    }

    /**
     * @return the data
     */
    public MainData getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(MainData data) {
        this.data = data;
    }

    public PText getStatTitle() {
        return statTitle;
    }

    public void setStatTitle(PText statTitle) {
        this.statTitle = statTitle;
    }

    class DeviceLeftClickMenu extends JPopupMenu {

        public DeviceLeftClickMenu() {

            JMenuItem pingItem = new JMenuItem("Выполнить команду ping");
            pingItem.setEnabled(true);
            pingItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        if (OS_Info.isWindows()) {
                            String[] commands = {"cmd.exe", "/c", "ping", selectedDevice.getIpAddress()};
                        } else {
                            String[] commands = {"xterm", "-e", "ping", selectedDevice.getIpAddress()};
                            Runtime.getRuntime().exec(commands);
                        }

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            });
            add(pingItem);

            final JMenuItem wiItem = new JMenuItem("Открыть web интерфейс устройства");
            wiItem.setEnabled(true);
            wiItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String[] commands = {"firefox", "http://" + selectedDevice.getIpAddress()};
                        Runtime.getRuntime().exec(commands);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            if (!(selectedDevice instanceof PhoneServer)) {
                add(wiItem);
            }
        }
    }

    private void redrawEdgesRecursively(IDevice device) {
        //device.determineEdgeStyleFromState();
        device.determineEdgeStyleFromPing();
        if (device.getChildren() != null) {
            for (IDevice childDevice : device.getChildren()) {
                redrawEdgesRecursively(childDevice);
            }
        }
    }

    private void createRandomNodeStateActivity() {
        changeRandomNodeStateActivity = new PActivity(-1, 1000) {
            protected void activityStep(long time) {
                super.activityStep(time);

                for (int i = 0; i < 20; i++) {
                    int randomDeviceIndex = randInt(0, data.getAllDevices().size() - 1);
                    IDevice randomDevice = new ArrayList<IDevice>(
                            data.getAllDevices()).get(randomDeviceIndex);
                    int randomStateIndex = new Random().nextInt(DeviceState.values().length);
                    randomDevice.setState(DeviceState.values()[randomStateIndex]);
                    selectDevice(randomDevice.getDeviceImage());
                }

                try {
                    setTableData();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        };
        nodeLayer.addActivity(changeRandomNodeStateActivity);
    }

    private void stopActivity() {
        if (changeRandomNodeStateActivity != null) {
            changeRandomNodeStateActivity.terminate();
        }
    }

    private void deleteDevice(IDevice device) throws IOException {

        // disconnect device from its parent
        if (device.getParent() != null) {
            device.getParent().getChildren().remove(device);
        }
        // disconnect device from its children
        if (device.getChildren() != null) {
            for (IDevice child : device.getChildren()) {
                child.setParent(null);
                edgeLayer.removeChild(child.getDeviceImage().getEdge());
                child.getDeviceImage().setEdge(null);
            }
        }
        // delete image
        nodeLayer.removeChild(device.getDeviceImage());
        // delete parent edge
        edgeLayer.removeChild(device.getDeviceImage().getEdge());
        device.getDeviceImage().setEdge(null);

        data.getDevicesMap().remove(device.getId());

        setTableData();
        selectDevice(data.getAllDevices().iterator().next().getDeviceImage());
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive. The
     * difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param max Maximim value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @para m min Minimim value
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void createMenu() {
        class Foo implements Serializable {

            private static final long serialVersionUID = -3408553456377559492L;
        }
        // create menu bar
        JMenuBar menuBar = new JMenuBar();

        setJMenuBar(menuBar);

        JMenu menuAbout = new JMenu("Справка");
        menuBar.add(menuAbout);

        JMenuItem menuItemAbout = new JMenuItem("О приложении");
        menuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        " Мониторинг сетевой инфраструктуры изделия МДА БСОРИ (alpha версия 1.0.4 )\n\n"
                                + "Приложение отправляет запросы и получает ответы по протоколу SNMP версии 2 \n"
                                + "на коммутаторы(БСК, БСКП), сервер(БУВО), точки доступа(ББПИ), контроллер ББПИ(КТД)\n"
                                + "и выводит результаты на графический интерфейс пользователя:\n"
                                + "Красный цвет устройств - БУВО не получает ответ от опрашиваемых устройств \n"
                                + "Зеленый цвет устройств - БУВО получает ответ от опрашиваемых устройств \n"
                                + "На панели справа - информация о местоположении устройства на корабле и местоположение его ЗИП-О",
                        "О Приложении...",
                        1

                );
            }
        });
        menuAbout.add(menuItemAbout);

    }

    /**
     * Zoom handler with restrictions for max and min zoom scale.
     *
     * @author
     */
    static class CustomZoomHandler extends PZoomEventHandler {

        /**
         * A constant used to adjust how sensitive the zooming will be to mouse
         * movement. The larger the number, the more each delta pixel will
         * affect zooming.
         */
        private static final double ZOOM_SENSITIVITY = 0.001;
        private double minScale = 0.5;
        private double maxScale = 5;
        private Point2D viewZoomPoint;

        /**
         * Records the start point of the zoom. Used when calculating the delta
         * for zoom speed.
         *
         * @param event event responsible for starting the zoom interaction
         */
        protected void dragActivityFirstStep(final PInputEvent event) {
            viewZoomPoint = event.getPosition();
        }

        protected void dragActivityStep(final PInputEvent event) {
            final PCamera camera = event.getCamera();
            final double dx = event.getCanvasPosition().getX() - getMousePressedCanvasPoint().getX();
            double scaleDelta = 1.0 + ZOOM_SENSITIVITY * dx;

            final double currentScale = camera.getViewScale();
            final double newScale = currentScale * scaleDelta;

            if (newScale < minScale) {
                scaleDelta = minScale / currentScale;
            }
            if (maxScale > 0 && newScale > maxScale) {
                scaleDelta = maxScale / currentScale;
            }

            camera.scaleViewAboutPoint(scaleDelta, viewZoomPoint.getX(), viewZoomPoint.getY());
        }

    }

    /**
     * Opens a web page using Firefox browser,
     * enters login name/password and tries to login.
     */
//    private void loginToPage() {
//
//        String username = "admin";
//        String password = "Marine2Air";
//        // Create a new instance of the Firefox driver
//        // Notice that the remainder of the code relies on the interface,
//        // not the implementation.
//        WebDriver driver = new FirefoxDriver();
//        // And now use this to visit Google
//        driver.get("http://10.168.2.50/login.php");
//        WebElement userNameElement = driver.findElement(By.id("user_name"));
//        // Enter user name
//        userNameElement.sendKeys(username);
//
//        // Find the 'password' text input element by its name attribute 'j_password'
//        WebElement passwordElement = driver.findElement(By.id("password"));
//        // Enter password
//        passwordElement.sendKeys(password);
//        //Click LOGIN button
//        driver.findElement(By.className("enablebuttonstyle")).click();
//    }
//
//    private void rebootSNMP() throws InterruptedException {
//        boolean acceptNextAlert = true;
//        String username = "admin";
//        String password = "Marine2Air";
//        // Create a new instance of the Firefox driver
//        // Notice that the remainder of the code relies on the interface,
//        // not the implementation.
//        WebDriver driver = new FirefoxDriver();
//        // And now use this to visit Google
//        driver.get("http://10.168.2.50/login.php");
//        driver.findElement(By.id("user_name")).clear();
//        driver.findElement(By.id("user_name")).sendKeys("admin");
//        driver.findElement(By.id("password")).clear();
//        driver.findElement(By.id("password")).sendKeys("Marine2Air");
//        driver.findElement(By.className("enablebuttonstyle")).click();
//
//        try {
//            Alert alert = driver.switchTo().alert();
//            String alertText = alert.getText();
//            if (acceptNextAlert) {
//                alert.accept();
//            } else {
//                alert.dismiss();
//            }
//            alertText = alert.getText();
//        } finally {
//            acceptNextAlert = true;
//        }

    //driver.findElement(By.cssSelector("input.enablebuttonstyle")).click();
    //driver.findElement(By.linkText("Access Point")).click();
    //driver.findElement(By.cssSelector("#TD_Main_2 > a.TertiaryNav > strong")).click();
    // ERROR: Caught exception [Error: locator strategy either id or name must be specified explicitly.]
    //driver.findElement(By.id("ManagedAccessPoint-status")).click();
    //driver.findElement(By.id("ManagedAccessPoint-status")).click();
    //driver.findElement(By.cssSelector("Maintenance")).click();
    //driver.findElement(By.cssSelector("Remote Management")).click();
    //driver.findElement(By.name("snmp")).click();
//}
}

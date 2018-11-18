package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import java.awt.Color;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * 
 */

public class database extends javax.swing.JFrame {
	
Connection myDbConn = getDBConnection();
    public static DefaultListModel DList1 = new DefaultListModel();
    public static DefaultListModel DList2 = new DefaultListModel();
    public static DefaultListModel DList3 = new DefaultListModel();
    public static ArrayList<String> CountryList = new ArrayList<String>();
    public static ArrayList<String> LocationList = new ArrayList<String>();
    public static ArrayList<String> MovieTagList = new ArrayList<String>();
    public database() {
        initComponents();
        DefaultListModel DList = new DefaultListModel();
         ArrayList<String> genereList = getGenreList(myDbConn);
        
        for (int i = 0; i < genereList.size(); i++) {
            DList.addElement(genereList.get(i));
        }
         for (int i = 0; i < genereList.size(); i++) {
            DList.addElement(genereList.get(i));
        }
        jList1.setModel(DList);
        jList2.setModel(DList1);
        jList3.setModel(DList2);
        jList4.setModel(DList3);
    }
    public Connection getDBConnection() {
        try {
            
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        } catch (ClassNotFoundException e) {
            
            System.out.println("No Oracle JDBC Driver found");
            e.printStackTrace();
        }
        System.out.println("Oracle JDBC Driver Registered!");
        
        Connection myConn = null;
        
        try {
            
        	 String host = "localhost";
		        String port = "1521";
		        String dbName = "orcl";
		        String userName = "scott";
		        String password = "oracle";

		        // Construct the JDBC URL
		        String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + "/" + dbName;
		        myConn = DriverManager.getConnection(dbURL, userName, password);
        } catch (SQLException e) {
            
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        
        return myConn;
    }
    
    public ArrayList<String> getGenreList(Connection con) {
        
        ArrayList<String> genreList = new ArrayList<String>();
        
        try {
            
            String selectQuery = "SELECT DISTINCT GENRE FROM MOVIE_GENRES ORDER BY GENRE";
            //jTextArea4.setText(selectQuery);
            Statement createStat = con.createStatement();
            ResultSet resultSet = createStat.executeQuery(selectQuery);
            // int a = 1;
            if (resultSet != null) {
                while (resultSet.next()) {
                    genreList.add(resultSet.getString(1));
                    //    a++;
                }
            }
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        return genreList;
    }
    
    public ArrayList<String> getCountryList(Connection conn) {
        
        ArrayList<String> CountryList = new ArrayList<String>();
        
        try {
            
            String selectQuery = "SELECT DISTINCT COUNTRY FROM MOVIE_COUNTRIES C, MOVIE_GENRES G ,"
                    + " MOVIES M WHERE G.MOVIEID = M.MID AND C.MOVIEID = M.MID"
                    + " AND  G.GENRE IN(" + getStrFromList(jList1.getSelectedValuesList()) + ")"
                    + " ORDER BY COUNTRY";
            //jTextArea4.setText(selectQuery);
            
            Statement createStat = conn.createStatement();
            System.out.println(selectQuery);
            //genrebuttonquery = genrebuttonquery + selectquery + "\n";
            ResultSet result = createStat.executeQuery(selectQuery);

            // int a = 1;
            if (result != null) {
                while (result.next()) {
                    CountryList.add(result.getString(1));
                    //    a++;
                }
            }
        } catch (SQLException e) {
            jTextArea4.setText("");
          
            //e.printStackTrace();
        }
        return CountryList;
    }
    
    public ArrayList<String> getLocationList(Connection conn) {
        
        ArrayList<String> LocationList = new ArrayList<String>();
        
        try {
            
            String selectQuery;
            if(jList2.getSelectedValuesList().isEmpty()){
                selectQuery = "SELECT DISTINCT L.LOCATION1 FROM MOVIE_LOCATIONS L, MOVIE_GENRES G"
                    + " WHERE G.MOVIEID = L.MOVIEID "
                    + " AND  G.GENRE IN(" + getStrFromList(jList1.getSelectedValuesList()) + ") ORDER BY L.LOCATION1";
            }
            else{
                selectQuery = "SELECT DISTINCT L.LOCATION1 FROM MOVIE_LOCATIONS L"
                        + " WHERE L.MOVIEID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G WHERE G.MOVIEID = C.MOVIEID"
                        + " AND  G.GENRE IN(" + getStrFromList(jList1.getSelectedValuesList()) + ")"
                        + " AND C.COUNTRY IN (" + getStrFromList(jList2.getSelectedValuesList()) +")) ORDER BY L.LOCATION1";
            }
            //jTextArea4.setText(selectQuery);
            Statement createStat = conn.createStatement();
            System.out.println(selectQuery);
            //genrebuttonquery = genrebuttonquery + selectquery + "\n";
            ResultSet result = createStat.executeQuery(selectQuery);

            // int a = 1;
            if (result != null) {
                while (result.next()) {
                    LocationList.add(result.getString(1));
                    //    a++;
                }
            }
        } catch (SQLException e) {
            jTextArea4.setText("");
          
            //e.printStackTrace();
        }
        return LocationList;
    }
    public ArrayList<String> getMovieTagList(Connection conn) {
        
        ArrayList<String> MovieTagList = new ArrayList<String>();
        
        try {
            
            String selectQuery;
            Date date = jDateChooser1.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int fromYear = cal.get(Calendar.YEAR);
            Date date2 = jDateChooser2.getDate();
            cal.setTime(date2);
            int toYear = cal.get(Calendar.YEAR);
            selectQuery = "SELECT DISTINCT T.TAG_TEXT FROM TAGS T, MOVIES M, MOVIE_TAGS MT " 
                       +  "WHERE T.TAGID = MT.TAGID AND M.MID = MT.MOVIEID AND "
                       +  "M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                       +  " AND M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                       +  " AND M.YEAR>"+fromYear+" AND M.YEAR<"+toYear
                       +  " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND " 
                       +  "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                       +  "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND G.GENRE IN ("+ getStrFromList(jList1.getSelectedValuesList())+")"
                       +  " AND C.COUNTRY IN (" + getStrFromList(jList2.getSelectedValuesList())+ ") AND L.LOCATION1 IN (" + getStrFromList(jList3.getSelectedValuesList())+ "))"
                       +  " ORDER BY T.TAG_TEXT";
            if(jComboBox1.getSelectedItem().equals("OR")){
                //System.out.println("Inside OR");
                if(jList1.getSelectedValuesList().size()>1 ){
                    
                    
                    List<String> genreTempList = jList1.getSelectedValuesList(); 
                    String genreOrCondition = "G.GENRE = ";
                    for(int i=0;i<genreTempList.size();i++) {
                        genreOrCondition = genreOrCondition + "'" +genreTempList.get(i) + "' OR G.GENRE = " ;
                    }
                    String orGenre = genreOrCondition.substring(0, genreOrCondition.length()-14);
                    if(jList2.getSelectedValuesList().size()>1){
                        List<String> countryTempList = jList2.getSelectedValuesList(); 
                        String countryOrCondition = "C.Country = ";
                        for(int i=0;i<countryTempList.size();i++) {
                            countryOrCondition = countryOrCondition + " '" +countryTempList.get(i) + "' OR C.Country = " ;
                        }
                        String orCountry = countryOrCondition.substring(0, countryOrCondition.length()-16);
                        if(jList3.getSelectedValuesList().size()>1){
                            List<String> locationTempList = jList3.getSelectedValuesList(); 
                            String locationOrCondition = "L.LOCATION1 = ";
                            for(int i=0;i<genreTempList.size();i++) {
                                locationOrCondition = locationOrCondition + "'" +locationTempList.get(i) + "' OR L.LOCATION1 = " ;
                            }
                            String orLocation = locationOrCondition.substring(0, locationOrCondition.length()-18);
                    
                            selectQuery = "SELECT DISTINCT T.TAG_TEXT FROM TAGS T, MOVIES M, MOVIE_TAGS MT " 
                            +  "WHERE T.TAGID=MT.TAGID AND M.MID = MT.MOVIEID AND "
                            +  "M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                            +  " AND M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                            +  " AND M.YEAR>"+fromYear+" AND M.YEAR<"+toYear
                            +  " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND " 
                            +  "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                            +  "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND " 
                            +  "(" + orGenre + ") AND (" + orCountry + ") AND (" + orLocation+ "))"
                            +  "ORDER BY T.TAG_TEXT";
                        }
                        else{
                            selectQuery = "SELECT DISTINCT T.TAG_TEXT FROM TAGS T, MOVIES M, MOVIE_TAGS MT " 
                            +  "WHERE T.TAGID=MT.TAGID AND M.MID = MT.MOVIEID AND "
                            +  "M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                            +  " AND M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                            +  " AND M.YEAR>"+fromYear+" AND M.YEAR<"+toYear
                            +  " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND " 
                            +  "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                            +  "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND " 
                            +  "(" + orGenre + ") AND (" + orCountry + ") AND L.LOCATION1 IN (" + getStrFromList(jList3.getSelectedValuesList())+ "))"
                            +  "ORDER BY T.TAG_TEXT";
                        }
                        
                    }
                    else{
                        selectQuery = "SELECT DISTINCT T.TAG_TEXT FROM TAGS T, MOVIES M, MOVIE_TAGS MT " 
                            +  "WHERE T.TAGID=MT.TAGID AND M.MID = MT.MOVIEID AND "
                            +  "M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                            +  " AND M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                            +  " AND M.YEAR>"+fromYear+" AND M.YEAR<"+toYear
                            +  " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND " 
                            +  "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                            +  "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND " 
                            +  "(" + orGenre + ") AND C.COUNTRY IN (" + getStrFromList(jList2.getSelectedValuesList())+") AND L.LOCATION1 IN (" + getStrFromList(jList3.getSelectedValuesList())+ "))"
                            +  "ORDER BY T.TAG_TEXT";
                    }
                    
                    
                }
                else{
                        selectQuery = "SELECT DISTINCT T.TAG_TEXT FROM TAGS T, MOVIES M, MOVIE_TAGS MT " 
                            +  "WHERE T.TAGID=MT.TAGID AND M.MID = MT.MOVIEID AND "
                            +  "M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                            +  " AND M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                            +  " AND M.YEAR>"+fromYear+" AND M.YEAR<"+toYear
                            +  " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND " 
                            +  "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                            +  "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND G.GENRE IN " 
                            +  "(" + getStrFromList(jList1.getSelectedValuesList()) + ") AND C.COUNTRY IN (" + getStrFromList(jList2.getSelectedValuesList())+") AND L.LOCATION1 IN (" + getStrFromList(jList3.getSelectedValuesList())+ "))"
                            +  "ORDER BY T.TAG_TEXT";
                }
            }
            
            jTextArea4.setText(selectQuery);
            Statement createStat = conn.createStatement();
            System.out.println(selectQuery);
            //System.out.println("br result set");
            //genrebuttonquery = genrebuttonquery + selectquery + "\n";
            ResultSet result = createStat.executeQuery(selectQuery);
            //System.out.println("After Result set");
            // int a = 1;
            if (result != null) {
                while (result.next()) {
                    MovieTagList.add(result.getString(1));
                    //    a++;
                }
            }
        } catch (SQLException e) {
            jTextArea4.setText("");
            e.printStackTrace();
            //e.printStackTrace();
        }
        return MovieTagList;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton1.setBackground(Color.WHITE);
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jPanel7 = new javax.swing.JPanel();
        jPanel7.setBackground(new Color(255, 255, 255));
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel8.setBackground(new Color(255, 255, 255));
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel10.setBackground(new Color(255, 255, 255));
        jLabel9 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setBackground(new Color(0, 102, 204));

        jPanel2.setBackground(new Color(0, 102, 204));
        jPanel2.setName("");

        jTextArea4.setEditable(false);
        jTextArea4.setColumns(20);
        jTextArea4.setLineWrap(true);
        jTextArea4.setRows(5);
        jTextArea4.setOpaque(false);
        jScrollPane5.setViewportView(jTextArea4);

        jTextArea5.setEditable(false);
        jTextArea5.setColumns(20);
        jTextArea5.setLineWrap(true);
        jTextArea5.setRows(5);
        jTextArea5.setOpaque(false);
        jScrollPane6.setViewportView(jTextArea5);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Execute Query");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        
        JPanel panel_5 = new JPanel();
        panel_5.setBackground(new Color(255, 255, 255));
        
        lblNewLabel_6 = new JLabel("Query Display:-");
        lblNewLabel_6.setForeground(new Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2Layout.setHorizontalGroup(
        	jPanel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel2Layout.createSequentialGroup()
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel2Layout.createSequentialGroup()
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(jPanel2Layout.createSequentialGroup()
        							.addContainerGap()
        							.addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 822, GroupLayout.PREFERRED_SIZE)
        							.addGap(18))
        						.addGroup(jPanel2Layout.createSequentialGroup()
        							.addGap(17)
        							.addComponent(lblNewLabel_6)))
        					.addGap(18))
        				.addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        					.addContainerGap(382, Short.MAX_VALUE)
        					.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
        					.addGap(371)))
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        				.addComponent(jScrollPane6, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
        			.addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
        	jPanel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel2Layout.createSequentialGroup()
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(lblNewLabel_6)
        				.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(Alignment.LEADING, jPanel2Layout.createSequentialGroup()
        					.addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
        				.addComponent(jScrollPane6, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_5.setLayout(null);
        
        JLabel lblNewLabel_5 = new JLabel("Query Results");
        lblNewLabel_5.setBounds(0, 0, 270, 29);
        lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
        panel_5.add(lblNewLabel_5);
        jPanel2.setLayout(jPanel2Layout);

        jPanel1.setBackground(new Color(0, 102, 204));

        jPanel3.setBackground(new Color(0, 102, 204));

        jPanel6.setBackground(Color.WHITE);
        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setBackground(new java.awt.Color(102, 102, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Search between Attributes' Values");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select AND,OR", "AND", "OR" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jList3.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList3);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jList2);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Value:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select relation", "=", "<", ">", "<=", ">=" }));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Rating: ");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Num of Reviews");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select relation", "=", "<", ">", "<=", ">=" }));

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Value:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7Layout.setHorizontalGroup(
        	jPanel7Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel7Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel7Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
        					.addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jComboBox3, 0, 156, Short.MAX_VALUE))
        				.addGroup(Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
        					.addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))
        				.addGroup(jPanel7Layout.createSequentialGroup()
        					.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jComboBox2, 0, 156, Short.MAX_VALUE))
        				.addGroup(Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
        					.addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
        	jPanel7Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel7Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jComboBox2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        			.addGap(5)
        			.addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jComboBox3, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        			.addGap(5)
        			.addGroup(jPanel7Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel7.setLayout(jPanel7Layout);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel7.setText("From:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel8.setText("To:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Tag Weight: ");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select relation", "=", "<", ">", "<=", ">=" }));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Value:");

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10Layout.setHorizontalGroup(
        	jPanel10Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel10Layout.createSequentialGroup()
        			.addGap(18)
        			.addGroup(jPanel10Layout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(jLabel9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(jLabel10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addGap(7)
        			.addGroup(jPanel10Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jComboBox4, 0, 142, Short.MAX_VALUE))
        			.addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
        	jPanel10Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel10Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel10Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel9)
        				.addComponent(jComboBox4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
        			.addGroup(jPanel10Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap())
        );
        jPanel10.setLayout(jPanel10Layout);

        jList4.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList4MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jList4);

        jButton2.setText("Generate Tag Values");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        
        panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        
        panel_1 = new JPanel();
        panel_1.setBackground(new Color(255, 255, 255));
        
        panel_2 = new JPanel();
        panel_2.setBackground(new Color(255, 255, 255));
        
        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(255, 255, 255));
        
        panel_4 = new JPanel();
        panel_4.setBackground(new Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3Layout.setHorizontalGroup(
        	jPanel3Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel3Layout.createSequentialGroup()
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING, false)
        				.addComponent(jPanel6, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addGroup(jPanel3Layout.createSequentialGroup()
        					.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(jPanel3Layout.createSequentialGroup()
        							.addGap(6)
        							.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        						.addGroup(jPanel3Layout.createSequentialGroup()
        							.addContainerGap()
        							.addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(jPanel10, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        				.addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        				.addComponent(jPanel7, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel3Layout.createSequentialGroup()
        					.addGap(57)
        					.addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
        					.addContainerGap())
        				.addGroup(jPanel3Layout.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jScrollPane4, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
        						.addGroup(jPanel3Layout.createSequentialGroup()
        							.addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING)
        								.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
        								.addComponent(jPanel8, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
        							.addContainerGap())))))
        );
        jPanel3Layout.setVerticalGroup(
        	jPanel3Layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(jPanel3Layout.createSequentialGroup()
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        				.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        				.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel3Layout.createSequentialGroup()
        					.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jScrollPane3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        						.addComponent(jScrollPane2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        						.addComponent(jScrollPane1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jPanel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        				.addGroup(Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
        					.addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING)
        						.addGroup(jPanel3Layout.createSequentialGroup()
        							.addComponent(jPanel7, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jPanel10, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
        						.addGroup(jPanel3Layout.createSequentialGroup()
        							.addComponent(jPanel8, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jScrollPane4, GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jButton2)
        					.addContainerGap())))
        );
        panel_4.setLayout(null);
        
        lblNewLabel_4 = new JLabel("Movie Year");
        lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_4.setBounds(0, 0, 269, 38);
        panel_4.add(lblNewLabel_4);
        panel_3.setLayout(null);
        
        JLabel lblNewLabel_3 = new JLabel("Critic's Rating");
        lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_3.setBounds(0, 0, 211, 33);
        panel_3.add(lblNewLabel_3);
        panel_2.setLayout(null);
        
        JLabel lblNewLabel_2 = new JLabel("Filming Location");
        lblNewLabel_2.setBounds(0, 0, 193, 33);
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
        panel_2.add(lblNewLabel_2);
        panel_1.setLayout(null);
        
        JLabel lblNewLabel_1 = new JLabel("Countries");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setBounds(6, 0, 181, 33);
        panel_1.add(lblNewLabel_1);
        panel.setLayout(null);
        
        lblNewLabel = new JLabel("Genres");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(6, 0, 187, 33);
        panel.add(lblNewLabel);
        jPanel3.setLayout(jPanel3Layout);

        jPanel4.setBackground(new Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Movie");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(274, 274, 274)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(169, 169, 169))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, 1080, Short.MAX_VALUE)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        jPanel1.setLayout(jPanel1Layout);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
        if (!jList1.getSelectedValuesList().isEmpty()) {
            jTextArea4.setText("");
            CountryList.clear();
            LocationList.clear();
            CountryList = getCountryList(myDbConn);
            LocationList = getLocationList(myDbConn);
            
            
            DList1.removeAllElements();
            DList2.removeAllElements();
            
            for (int i = 0; i < CountryList.size(); i++) {
                DList1.addElement(CountryList.get(i));
            }
            for (int i = 0; i < LocationList.size(); i++) {
                DList2.addElement(LocationList.get(i));
            }
        }
    }                                   

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
        if (!jList1.getSelectedValuesList().isEmpty()) {
            jTextArea4.setText("");
            LocationList.clear();
            LocationList = getLocationList(myDbConn);
            
            DList2.removeAllElements();
            for (int i = 0; i < LocationList.size(); i++) {
                DList2.addElement(LocationList.get(i));
            }
        }
    }                                   

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {                                      
        // TODO add your handling code here:
        try {
            
            String selectQuery;
            Date date = jDateChooser1.getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int fromYear = cal.get(Calendar.YEAR);
            Date date2 = jDateChooser2.getDate();
            cal.setTime(date2);
            int toYear = cal.get(Calendar.YEAR);
            if(jComboBox1.getSelectedItem().equals("OR")){
                if(jList1.getSelectedValuesList().size()>1){
                    
                    List<String> genreTempList = jList1.getSelectedValuesList(); 
                    String genreOrCondition = "G.GENRE = ";
                    for(int i=0;i<genreTempList.size();i++) {
                        genreOrCondition = genreOrCondition + "'" +genreTempList.get(i) + "' OR G.GENRE = " ;
                    }
                    String orGenre = genreOrCondition.substring(0, genreOrCondition.length()-14);
                    if(jList2.getSelectedValuesList().size()>1){
                        
                        List<String> countryTempList = jList2.getSelectedValuesList(); 
                        String countryOrCondition = "C.COUNTRY = ";
                        for(int i=0;i<countryTempList.size();i++) {
                            countryOrCondition = countryOrCondition + " '" +countryTempList.get(i) + "' OR C.COUNTRY = " ;
                        }
                        String orCountry = countryOrCondition.substring(0, countryOrCondition.length()-16);
                       
                        if(jList3.getSelectedValuesList().size()>1){
                            
                            List<String> locationTempList = jList3.getSelectedValuesList(); 
                            String locationOrCondition = "L.LOCATION1 = ";
                            for(int i=0;i<genreTempList.size();i++) {
                                locationOrCondition = locationOrCondition + "'" +locationTempList.get(i) + "' OR L.LOCATION1 = " ;
                            }
                            String orLocation = locationOrCondition.substring(0, locationOrCondition.length()-18);
                         
                            
                            if(jList4.getSelectedValuesList().size()>1){
                                 
                                List<String> tagTempList = jList4.getSelectedValuesList(); 
                                String tagOrCondition = "T.TAG_TEXT = ";
                                for(int i=0;i<tagTempList.size();i++) {
                                    tagOrCondition = tagOrCondition + "'" +tagTempList.get(i) + "' OR T.TAG_TEXT = " ;
                                }
                                String orTag = tagOrCondition.substring(0, tagOrCondition.length()-17);
                                selectQuery = "SELECT DISTINCT  M.TITLE, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1," 
                               + "ROUND(AVG(M.rtAllCriticsRating+M.rtTopCriticsRating+M.rtAudienceRating)/3.0,2) AS AverageRating, " 
                               + "ROUND(AVG(M.rtAllCriticsNumReviews+M.rtTopCriticsNumReviews+M.rtAudienceNumRatings)/3.0,2) AS AverageReviews " 
                               + "FROM MOVIES M,MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L, TAGS T, MOVIE_TAGS MT " 
                               + "WHERE T.TAGID = MT.TAGID AND M.MID = MT.MOVIEID AND "
                               + "G.MOVIEID = M.MID AND C.MOVIEID = M.MID AND L.MOVIEID = M.MID AND " 
                               + " M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                               + " AND M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                               + " AND M.YEAR > "+fromYear+" AND M.YEAR < "+toYear
                               + " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND " 
                               + "(" + orTag + ") AND "
                               + "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                               + "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND " 
                               + "(" + orGenre + ") AND (" + orCountry + ") AND (" + orLocation+ "))"
                               + "GROUP BY G.MOVIEID, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1,M.TITLE"
                               + " ORDER BY M.TITLE";
                            }
                            else{
                                selectQuery = "SELECT DISTINCT  M.TITLE, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1," 
                               + "ROUND(AVG(M.rtAllCriticsRating+M.rtTopCriticsRating+M.rtAudienceRating)/3.0,2) AS AverageRating, " 
                               + "ROUND(AVG(M.rtAllCriticsNumReviews+M.rtTopCriticsNumReviews+M.rtAudienceNumRatings)/3.0,2) AS AverageReviews " 
                               + "FROM MOVIES M,MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L, TAGS T, MOVIE_TAGS MT " 
                               + "WHERE T.TAGID = MT.TAGID AND M.MID = MT.MOVIEID AND "
                               + "G.MOVIEID = M.MID AND C.MOVIEID = M.MID AND L.MOVIEID = M.MID AND " 
                               + " M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                               + " AND M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                               + " AND M.YEAR > "+fromYear+" AND M.YEAR < "+toYear
                               + " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND T.TAG_TEXT IN " 
                               + "(" + getStrFromList(jList4.getSelectedValuesList()) + ") AND "
                               + "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                               + "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND " 
                               + "(" + orGenre + ") AND (" + orCountry + ") AND (" + orLocation+ "))"
                               + "GROUP BY G.MOVIEID, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1,M.TITLE"
                               + " ORDER BY M.TITLE";
                            }
                          
                    }
                    else{
                        selectQuery = "SELECT DISTINCT  M.TITLE, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1," 
                               + "ROUND(AVG(M.rtAllCriticsRating+M.rtTopCriticsRating+M.rtAudienceRating)/3.0,2) AS AverageRating, " 
                               + "ROUND(AVG(M.rtAllCriticsNumReviews+M.rtTopCriticsNumReviews+M.rtAudienceNumRatings)/3.0,2) AS AverageReviews " 
                               + "FROM MOVIES M,MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L, TAGS T, MOVIE_TAGS MT " 
                               + "WHERE T.TAGID = MT.TAGID AND M.MID = MT.MOVIEID AND "
                               + "G.MOVIEID = M.MID AND C.MOVIEID = M.MID AND L.MOVIEID = M.MID AND " 
                               + " M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                               + " AND M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                               + " AND M.YEAR > "+fromYear+" AND M.YEAR < "+toYear
                               + " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND T.TAG_TEXT IN " 
                               + "(" + getStrFromList(jList4.getSelectedValuesList()) + ") AND "
                               + "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                               + "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND " 
                               + "(" + orGenre + ") AND (" + orCountry + ") AND L.LOCATION1 IN (" + getStrFromList(jList3.getSelectedValuesList())+ "))"
                               + "GROUP BY G.MOVIEID, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1,M.TITLE"
                               + " ORDER BY M.TITLE";
                    }
             
                }
                else{
                    selectQuery = "SELECT DISTINCT  M.TITLE, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1," 
                        + "ROUND(AVG(M.rtAllCriticsRating+M.rtTopCriticsRating+M.rtAudienceRating)/3.0,2) AS AverageRating, " 
                        + "ROUND(AVG(M.rtAllCriticsNumReviews+M.rtTopCriticsNumReviews+M.rtAudienceNumRatings)/3.0,2) AS AverageReviews " 
                        + "FROM MOVIES M,MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L, TAGS T, MOVIE_TAGS MT " 
                        + "WHERE T.TAGID = MT.TAGID AND M.MID = MT.MOVIEID AND "
                        + "G.MOVIEID = M.MID AND C.MOVIEID = M.MID AND L.MOVIEID = M.MID AND " 
                        + " M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                        + " AND M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                        + " AND M.YEAR > "+fromYear+" AND M.YEAR < "+toYear
                        + " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND T.TAG_TEXT IN " 
                        + "(" + getStrFromList(jList4.getSelectedValuesList()) + ") AND "
                        + "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                        + "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND " 
                        + "(" + orGenre + ") AND C.COUNTRY IN (" + getStrFromList(jList2.getSelectedValuesList()) + ") AND L.LOCATION1 IN (" + getStrFromList(jList3.getSelectedValuesList())+ "))"
                        + "GROUP BY G.MOVIEID, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1,M.TITLE"
                        + " ORDER BY M.TITLE";
                }
            }
            else{
                    selectQuery = "SELECT DISTINCT  M.TITLE, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1," 
                        + "ROUND(AVG(M.rtAllCriticsRating+M.rtTopCriticsRating+M.rtAudienceRating)/3.0,2) AS AverageRating, " 
                        + "ROUND(AVG(M.rtAllCriticsNumReviews+M.rtTopCriticsNumReviews+M.rtAudienceNumRatings)/3.0,2) AS AverageReviews " 
                        + "FROM MOVIES M,MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L, TAGS T, MOVIE_TAGS MT " 
                        + "WHERE T.TAGID = MT.TAGID AND M.MID = MT.MOVIEID AND "
                        + "G.MOVIEID = M.MID AND C.MOVIEID = M.MID AND L.MOVIEID = M.MID AND " 
                        + " M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                        + " AND M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                        + " AND M.YEAR > "+fromYear+" AND M.YEAR < "+toYear
                        + " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND T.TAG_TEXT IN " 
                        + " (" + getStrFromList(jList4.getSelectedValuesList()) + ") AND "
                        + "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                        + "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND G.GENRE IN " 
                        + "(" + getStrFromList(jList1.getSelectedValuesList()) + ") AND C.COUNTRY IN (" + getStrFromList(jList2.getSelectedValuesList()) + ") AND L.LOCATION1 IN (" + getStrFromList(jList3.getSelectedValuesList())+ "))"
                        + "GROUP BY M.TITLE, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1 "
                        + " ORDER BY M.TITLE ";
            }
            
                        
            System.out.println(selectQuery);
            jTextArea4.setText(selectQuery);
            Statement createStat = myDbConn.createStatement();
            ResultSet result = createStat.executeQuery(selectQuery);
                    
                    
            boolean searchEmpty = true;
            jTextArea5.setText("");
            while (result.next()) {
                searchEmpty = false;
                jTextArea5.append("\n");
                jTextArea5.append(result.getString(1) + "  ");
                jTextArea5.append(result.getString(2) + "  ");
                jTextArea5.append(result.getInt(3) + "  ");
                jTextArea5.append(result.getString(4) + "  ");
                jTextArea5.append(result.getString(5) + "" + "  ");
                jTextArea5.append(result.getDouble(6) + "" + "  ");
                jTextArea5.append(result.getDouble(7) + "" + "  ");
                        
                        
                    }
                 
                    if (searchEmpty) {
                        jTextArea5.setText("The Selected Combination does not provide a result");
                    }

            }
            else{
                selectQuery = "SELECT DISTINCT  M.TITLE, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1," 
                        + "ROUND(AVG(M.rtAllCriticsRating+M.rtTopCriticsRating+M.rtAudienceRating)/3.0,2) AS AverageRating, " 
                        + "ROUND(AVG(M.rtAllCriticsNumReviews+M.rtTopCriticsNumReviews+M.rtAudienceNumRatings)/3.0,2) AS AverageReviews " 
                        + "FROM MOVIES M,MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L, TAGS T, MOVIE_TAGS MT " 
                        + "WHERE T.TAGID = MT.TAGID AND M.MID = MT.MOVIEID AND "
                        + "G.MOVIEID = M.MID AND C.MOVIEID = M.MID AND L.MOVIEID = M.MID AND " 
                        + " M.RTALLCRITICSRATING "+jComboBox2.getSelectedItem()+" "+jTextField1.getText()
                        + " AND M.RTALLCRITICSNUMREVIEWS "+jComboBox3.getSelectedItem()+" "+jTextField2.getText()
                        + " AND M.YEAR > "+fromYear+" AND M.YEAR < "+toYear
                        + " AND MT.TAGWEIGHT "+jComboBox4.getSelectedItem()+" "+jTextField3.getText()+" AND T.TAG_TEXT IN " 
                        + " (" + getStrFromList(jList4.getSelectedValuesList()) + ") AND "
                        + "M.MID IN (SELECT C.MOVIEID FROM MOVIE_COUNTRIES C, MOVIE_GENRES G, MOVIE_LOCATIONS L "
                        + "WHERE G.MOVIEID = C.MOVIEID AND L.MOVIEID = G.MOVIEID AND G.GENRE IN " 
                        + "(" + getStrFromList(jList1.getSelectedValuesList()) + ") AND C.COUNTRY IN (" + getStrFromList(jList2.getSelectedValuesList()) + ") AND L.LOCATION1 IN (" + getStrFromList(jList3.getSelectedValuesList())+ "))"
                        + "GROUP BY M.TITLE, G.GENRE, M.YEAR, C.COUNTRY,L.LOCATION1 "
                        + " ORDER BY M.TITLE ";
            }
            System.out.println(selectQuery);
            jTextArea4.setText(selectQuery);
            Statement createStat = myDbConn.createStatement();
            ResultSet result = createStat.executeQuery(selectQuery);
                    
                    
            boolean searchEmpty = true;
            jTextArea5.setText("");
            while (result.next()) {
                searchEmpty = false;
                jTextArea5.append("\n");
                jTextArea5.append(result.getString(1) + "  ");
                jTextArea5.append(result.getString(2) + "  ");
                jTextArea5.append(result.getInt(3) + "  ");
                jTextArea5.append(result.getString(4) + "  ");
                jTextArea5.append(result.getString(5) + "" + "  ");
                jTextArea5.append(result.getDouble(6) + "" + "  ");
                jTextArea5.append(result.getDouble(7) + "" + " ");
                        
                        
                    }
                 
                    if (searchEmpty) {
                        jTextArea5.setText("The Selected Combination does not provide a result");
                    }

        } catch (SQLException ex) {
                Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
            }
    }                                     

    private void jList4MouseClicked(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
    }                                   

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
       
    }                                           

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {                                      
        // TODO add your handling code here:
        if (!jList1.getSelectedValuesList().isEmpty() && !jList2.getSelectedValuesList().isEmpty() && !jList3.getSelectedValuesList().isEmpty() &&
                !jTextField1.getText().isEmpty() && !jTextField2.getText().isEmpty() && !jTextField3.getText().isEmpty() ) {
            jTextArea4.setText("");
            MovieTagList.clear();
            MovieTagList = getMovieTagList(myDbConn);
            
            DList3.removeAllElements();
            for (int i = 0; i < MovieTagList.size(); i++) {
                DList3.addElement(MovieTagList.get(i));
            }
        }
    }                                     

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(database.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(database.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(database.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(database.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new database().setVisible(true);
            }
        });
    }
    public String getStrFromList(List<String> strList) {
        String str = "";
        if (strList.size()==0){
            str = "''";
        }
        else{
        for (int i = 0; i < strList.size(); i++) {
            str = str + "'" + handleSpecialCharacter(strList.get(i)) + "'";
            if (i < strList.size() - 1) {
                str = str + ",";
            }
        }
        }
        
        return str;
        
    }
     public static String handleSpecialCharacter(String str) {
        
        String changedStr = null;
        if (str != null && !str.isEmpty()) {
            changedStr = str;
            changedStr = str.replaceAll("'", "''");
        }
        
        return changedStr;
        
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JList<String> jList4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private JPanel panel;
    private JLabel lblNewLabel;
    private JPanel panel_1;
    private JPanel panel_2;
    private JPanel panel_4;
    private JLabel lblNewLabel_4;
    private JLabel lblNewLabel_6;
}
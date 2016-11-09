/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metadata;

/**
 *
 * @author himos
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.postgresql.util.PSQLException;

import explelan.DBConnection;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class Metadata {
	//private int working_mem;
	private String table;
	private Connection connection;
	public enum StrDistanceMetric{JaroWinkler,Levenshtein,NGram};
        public double StrDisThrld=0.80;
        
	public Connection getConnection(String dbType, String dbAddress, String dbUser, String dbPassword) {
		Connection connection = null;
		try {
		    Class.forName("org." + dbType + ".Driver");
		} catch (ClassNotFoundException e) {
			connection = null;
		    System.out.println("DB driver not found");
		    e.printStackTrace();
		    return null;
		}
		
		//attempt to connect
		try {
			connection = DriverManager.getConnection(
					"jdbc:" + dbType + "://" + dbAddress, dbUser,
					dbPassword);
                        Statement stmt = connection.createStatement();
                        stmt.execute("set work_mem='" + 300 + "MB';");
		} catch (SQLException e) {
			connection = null;
			System.out.println("DB connection failed.");
			e.printStackTrace();
			return null;
		}
		return connection;
	}
	
	//public Metadata(String table, String dbType, String dbAddress, String dbUser, String dbPassword) {
        public Metadata(String tablee,DBConnection DBC ) {
		this.table = tablee;
		//this.working_mem = working_mem;
		this.connection = DBC.getConnection();
                //this.connection = this.getConnection(dbType, dbAddress, dbUser, dbPassword);
	}
	
        public void setConnection(Connection c) {
		this.connection = c;
	}
        public Map<String, String> getColumnAttributes() throws SQLException {
		
		Map<String, String> columnAttributes = new HashMap<String, String>();

		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();

			rs = dbmd.getColumns(null, null, table, null);

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			return columnAttributes;
		}
		
		
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			String type = rs.getString("TYPE_NAME");
                        //System.out.println(name+"\t"+ type);
			columnAttributes.put(name, type);
		}

		return columnAttributes;
	}
        
        
        //Added by Himos Get Dimessions
        public Map<String, String> getDimColumn() throws SQLException {
		
		Map<String, String> columnAttributes = new HashMap<String, String>();

		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();
			rs = dbmd.getColumns(null, null, table, null);
                     

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			return columnAttributes;
		}
		
	
		
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			String type = rs.getString("TYPE_NAME");
                        if (type.toLowerCase().startsWith("varchar")){
			columnAttributes.put(name, type);}
                       
		}

		return columnAttributes;
	}
        
        public String getAttrTypeColumn(String AttrNmae) throws SQLException {
		
		String Type="";

		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();
			rs = dbmd.getColumns(null, null, table, null);
                     

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			
		}
		
	
		
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			String type = rs.getString("TYPE_NAME");
                        if(AttrNmae.equalsIgnoreCase(name)){
                            
                     
                                if (type.toLowerCase().startsWith("varchar"))
                                Type="C";
                                else
                                Type="R";
                                        
                       
		}

		
	}
                return Type;
        }
        
        public DefaultTableModel getTableColumns() throws SQLException {
		
            DefaultTableModel ColumsMetadata=new DefaultTableModel();
             ColumsMetadata.addColumn("ColName");
             ColumsMetadata.addColumn("ColType");

		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();
			rs = dbmd.getColumns(null, null, table, null);
                     

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			
		}
		
	
		
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			String type = rs.getString("TYPE_NAME");
                        // Storing......
                                        Vector PredStatInf= new Vector();
                                        PredStatInf.add(name);
                                        PredStatInf.add(type);
                                        
                                        ColumsMetadata.addRow(PredStatInf); 

		
	}
                return ColumsMetadata;
        }
        
        

    
        
    
        // Added by Himos Get Measures
        public Map<String, String> getMeasureColumn() throws SQLException {
		
		Map<String, String> columnAttributes = new HashMap<String, String>();

		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();
			rs = dbmd.getColumns(null, null, table, null);

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			return columnAttributes;
		}
		
		
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			String type = rs.getString("TYPE_NAME");
                         if (!type.toLowerCase().startsWith("varchar")){
			columnAttributes.put(name, type);}
		}

		return columnAttributes;
	}

	public Integer getNumRows() throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
			
		String sqlQuery = "SELECT count(*) FROM " + table;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getInt(1);
		    
		    
		} catch (PSQLException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
        
        public Integer getNumRowsWhre(String Whre) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
			
		String sqlQuery = "SELECT count(*) FROM " + table+" "+Whre;
                //System.out.println(sqlQuery);
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getInt(1);
		    
		    
		} catch (PSQLException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}

	public Float getVariance(String columnName) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
			
		String sqlQuery = "SELECT var_pop(" + columnName + ") FROM " + table;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getFloat(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("var_pop") && e.getMessage().contains("does not exist")) {
			 	return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
        
          public Float getMax(String columnName,String Whre) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
			
		String sqlQuery = "SELECT max(" + columnName + ") FROM " + table+" "+Whre;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getFloat(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("Max") && e.getMessage().contains("does not exist")) {
			 	return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
          
          public Float getAVG(String columnName,String Whre) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
			
		String sqlQuery = "SELECT avg(" + columnName + ") FROM " + table+" "+Whre;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getFloat(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("Max") && e.getMessage().contains("does not exist")) {
			 	return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
          
          public Float getSum(String columnName,String Whre) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
			
		String sqlQuery = "SELECT sum(" + columnName + ") FROM " + table+" "+Whre;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getFloat(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("Max") && e.getMessage().contains("does not exist")) {
			 	return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
        
          public Float getMin(String columnName,String Whre) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
			
		String sqlQuery = "SELECT min(" + columnName + ") FROM " + table+" "+Whre;
                
           //     System.out.println(sqlQuery);
                       
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getFloat(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("Min") && e.getMessage().contains("does not exist")) {
			 	return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
          
          public int getAttsizeWidth(String Col) throws SQLException {
		
		int size=-1;

		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();
			rs = dbmd.getColumns(null, null, table, null);
                     

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			
		}
		
		
		
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			//String type = rs.getString("TYPE_NAME");
                        if (name.equals(Col)){
                            
                            size=rs.getInt("COLUMN_SIZE");
			return size;
                        }
                       
		}

		return size;
	}
          
        public Float getMesVariance(String columnName,String Whr) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
			
		String sqlQuery = "SELECT var_pop(" + columnName + ") FROM " + table+" "+Whr;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getFloat(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("var_pop") && e.getMessage().contains("does not exist")) {
			 	return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
        
        public double getMesVarianceNormalized(String columnName,String Whr,String NormalizeTo) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
                double NornalizeVal=0;
                if(NormalizeTo.equalsIgnoreCase("Max")){
                    NornalizeVal=getMax(columnName,Whr);
                    if(NornalizeVal==0.0) return 0;
                }
                else if	(NormalizeTo.equalsIgnoreCase("Sum")){
                        NornalizeVal=getSum(columnName,Whr);
                        if(NornalizeVal==0.0) return 0;
                }
		String sqlQuery = "SELECT var_pop(" + columnName + "/"+NornalizeVal +") FROM " + table+" "+Whr;
            //    System.out.println(sqlQuery);
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getFloat(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("var_pop") && e.getMessage().contains("does not exist")) {
			 	return 0;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return 0;
	}
        
        public Float getCorr(String columnName1,String columnName2) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
			
		String sqlQuery = "SELECT corr(" + columnName1+","+columnName2 + ") FROM " + table;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);
	
		    rs.next();
		 	return rs.getFloat(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("var_pop") && e.getMessage().contains("does not exist")) {
			 	return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
	
        public DefaultTableModel getColsSelectivity( ){
             DefaultTableModel model = new DefaultTableModel();		
            
            if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();
			rs = dbmd.getColumns(null, null, table, null);

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			//return columnAttributes;
		}
		
		try{
		String Stmt="Select ";
                int count=0;
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			//String type = rs.getString("TYPE_NAME");
                        if (name.startsWith("dim")){
                        String colstmt="count (distinct "+name+") as "+name+"_Cardnility, ";
                        Stmt=Stmt+colstmt;
                       count++;
                        }
		}
                rs.close();
                Stmt=Stmt.substring(0,Stmt.length()-2)+" from "+table.toString();
                int tblesize=getNumRows();
                //System.out.println(Stmt);
                ResultSet rsdata=null;
                rsdata=ExecuteQryWithRS(Stmt);
                //Save data 
               
                 model.addColumn("ColName");
                 model.addColumn("Cardinality");
                 model.addColumn("Selectvity");
                 model.addColumn("Density");
                 
                //Compute cols Stats
                while(rsdata.next()){
                    for (int i=1;i<=count;i++){
                        String Colname=rsdata.getMetaData().getColumnLabel(i);
                        
                        Colname=Colname.substring(0,Colname.indexOf("_cardnility"));
                        double colval=rsdata.getDouble(i);
                        double selectvity=colval/tblesize;
                        double density=1/colval;
                        Vector textData = new Vector();
                                textData.add(Colname);
                                textData.add(colval);
                                textData.add(selectvity);
                                textData.add(density);
                        System.out.println(Colname+"\t"+colval+"\t"+selectvity+"\t"+density);
                         model.addRow(textData );
                    }
                }
                } catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			//return columnAttributes;
		}
                return model;
        }
        
        public DefaultTableModel getColsSelectivity2( int ins, String WHRESTMT){
             DefaultTableModel model = new DefaultTableModel();		
            
            if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();
			rs = dbmd.getColumns(null, null, table, null);

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			//return columnAttributes;
		}
		
		try{
		String Stmt="Select ";
                int count=0;
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			//String type = rs.getString("TYPE_NAME");
                        if (name.startsWith("dim")){
                        String colstmt="count (distinct "+name+") as "+name+"_Cardnility, ";
                        Stmt=Stmt+colstmt;
                       count++;
                        }
		}
                rs.close();
                Stmt=Stmt.substring(0,Stmt.length()-2)+" from "+table.toString()+" "+WHRESTMT;
                double tblesize=0;
                if(WHRESTMT.isEmpty()&&WHRESTMT!=null){
                tblesize=getNumRowsWhre(WHRESTMT);
                    System.out.println(WHRESTMT+"+++++===="+tblesize);
                }
                else{
                 tblesize=getNumRows();}
                //System.out.println(Stmt);
                ResultSet rsdata=null;
                rsdata=ExecuteQryWithRS(Stmt);
                //Save data 
               
                 model.addColumn("ColName");
                 model.addColumn("Cardinality");
                 model.addColumn("Selectvity");
                 model.addColumn("Density");
                 
                //Compute cols Stats
                while(rsdata.next()){
                    String values="";
                    for (int i=1;i<=count;i++){
                        String Colname=rsdata.getMetaData().getColumnLabel(i);
                        
                        Colname=Colname.substring(0,Colname.indexOf("_cardnility"));
                        double colval=rsdata.getDouble(i);
                        double selectvity=colval/tblesize;
                        double density=1/colval;
                        Vector textData = new Vector();
                                textData.add(Colname);
                                textData.add(colval);
                                textData.add(selectvity);
                                textData.add(density);
                                if(ins==1){
                                
                                       values =values+"('"+Colname+"',"+colval+","+selectvity+","+density+",'"+table.toString()+"'),";
                                }
                        System.out.println(Colname+"\t"+colval+"\t"+selectvity+"\t"+density);
                         model.addRow(textData );
                         
                    }
                    if(ins==1){
                    values="insert into cols_stats (ColName,Cardinality,Selectvity,Density,tblname) values "+
                                 values.substring(0, values.length()-1)+" ";
                         System.out.println(values);
                         ExecuteQry("delete from cols_stats where tblname='"+table.toString()+"'");
                         ExecuteQry(values);
                    }
                }
                } catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			//return columnAttributes;
		}
                return model;
        }
        
        public DefaultTableModel getMesursVarPop( String WHRESTMT){
             DefaultTableModel model = new DefaultTableModel();		
            
            if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();
			rs = dbmd.getColumns(null, null, table, null);

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			//return columnAttributes;
		}
		
		try{
		//Save data 
               
                 model.addColumn("MesaureName");
                 model.addColumn("Var_PopQ");
                 model.addColumn("Var_PopD");
                 model.addColumn("Difference");
                 model.addColumn("MinQ");
                 model.addColumn("MaxQ");
                 model.addColumn("MinD");
                 model.addColumn("MaxD");
                 
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			//String type = rs.getString("TYPE_NAME");
                        if (name.startsWith("measure")){
                        double varpopQ=getMesVariance(name,WHRESTMT);
                        double varpopD=getVariance(name);
                        double MaxD=getMax(name,"");
                        double MinD=getMin(name,"");
                        double MaxQ=getMax(name,WHRESTMT);
                        double MinQ=getMin(name,WHRESTMT);
                        
                       Vector textData = new Vector();
                                textData.add(name);
                                textData.add(varpopQ);
                                textData.add(varpopD);
                                textData.add(varpopD-varpopQ);
                                textData.add(MinQ);
                                textData.add(MaxQ);
                                textData.add(MinD);
                                textData.add(MaxD);
                                
                        model.addRow(textData );
                        }
		}
                rs.close();
                
                
              
                } catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			//return columnAttributes;
		}
                return model;
        }
        
        public DefaultTableModel getDimDistinct( String WHRESTMT){
             DefaultTableModel model = new DefaultTableModel();		
            
            if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
                    
			dbmd = connection.getMetaData();
			rs = dbmd.getColumns(null, null, table, null);

		} catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			//return columnAttributes;
		}
		
		try{
		//Save data 
               
                 model.addColumn("DimName");
                 model.addColumn("NDistinct_Q");
                 model.addColumn("NDistinct_D");
                 model.addColumn("Difference");
                 
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			//String type = rs.getString("TYPE_NAME");
                        if (name.startsWith("dim")){
                        double varpopQ=getNumDistinctWhr(name,WHRESTMT);
                        double varpopD=getNumDistinct(name);
                       Vector textData = new Vector();
                                textData.add(name);
                                textData.add(varpopQ);
                                textData.add(varpopD);
                                textData.add(varpopD-varpopQ);
                        model.addRow(textData );
                        }
		}
                rs.close();
                
                
              
                } catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			//return columnAttributes;
		}
                return model;
        }
        
         public DefaultTableModel getDim_GroupSelectivity( Map<String, String> Dims, int ins, String WHRESTMT){
             DefaultTableModel model = new DefaultTableModel();		
            
           
		try{
		String Stmt="Select ";
                int count=0;
                  for (Map.Entry<String, String> columnAttribute1 : Dims.entrySet()){
		
			String name = columnAttribute1.getKey().toString();
			
		}
                
                Stmt=Stmt.substring(0,Stmt.length()-2)+" from "+table.toString()+" "+WHRESTMT;
                double tblesize=0;
                if(!WHRESTMT.isEmpty()&&WHRESTMT!=null){
                tblesize=getNumRowsWhre(WHRESTMT);}
                else{
                 tblesize=getNumRows();}
                //System.out.println(tblesize);
                ResultSet rsdata=null;
                rsdata=ExecuteQryWithRS(Stmt);
                //Save data 
               
                 model.addColumn("ColName");
                 model.addColumn("Cardinality");
                 model.addColumn("Selectvity");
                 model.addColumn("Density");
                 
                //Compute cols Stats
                while(rsdata.next()){
                    String values="";
                    for (int i=1;i<=count;i++){
                        String Colname=rsdata.getMetaData().getColumnLabel(i);
                        
                        Colname=Colname.substring(0,Colname.indexOf("_cardnility"));
                        if(Colname.startsWith("dim")){
                        double colval=rsdata.getDouble(i);
                        double selectvity=colval/tblesize;
                        double density=1/colval;
                        Vector textData = new Vector();
                                textData.add(Colname);
                                textData.add(colval);
                                textData.add(selectvity);
                                textData.add(density);
                                if(ins==1){
                                
                                       values =values+"('"+Colname+"',"+colval+","+selectvity+","+density+",'"+table.toString()+"'),";
                                }
                      //  System.out.println(Colname+"\t"+colval+"\t"+selectvity+"\t"+density);
                         model.addRow(textData );
                    }
                    }
                    if(ins==1){
                    values="insert into cols_stats (ColName,Cardinality,Selectvity,Density,tblname) values "+
                                 values.substring(0, values.length()-1)+" ";
                         System.out.println(values);
                         ExecuteQry("delete from cols_stats where tblname='"+table.toString()+"'");
                         ExecuteQry(values);
                    }
                }
                } catch (SQLException e) {
			System.out.println("Error in executing metadata query");
			e.printStackTrace();
			//return columnAttributes;
		}
                return model;
        }
       
    
        
     
	
	public Integer getNumDistinct(String columnName) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
					
		String sqlQuery = "SELECT count(distinct(" + columnName + ")) FROM " + table;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);

		    rs.next();
		    return rs.getInt(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("variance") && e.getMessage().contains("does not exist")) {
				return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
        public Integer getNumDistinctWhr(String columnName,String Whre) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
					
		String sqlQuery = "SELECT count(distinct(" + columnName + ")) FROM " + table +" "+Whre;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);

		    rs.next();
		    return rs.getInt(1);
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("variance") && e.getMessage().contains("does not exist")) {
				return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
        
        public String getDistinctValuesWhr(String columnName,String Whre) throws SQLException {
		
                //String values=" and "+columnName+" not in ('";
                String values="'";
		if (connection == null) {
			System.out.println("Connection null. Quit");
		}
		
					
		String sqlQuery = "SELECT distinct " + columnName + " FROM " + table +" "+Whre;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		    rs = stmt.executeQuery(sqlQuery);

		    while(rs.next()){
                          //results.add();
                          values=values+rs.getString(1)+"','";
                    }
                    values=values.substring(0,values.length()-2);
		    return values;
		    
		    
		} catch (PSQLException e) {
			if (e.getMessage().contains("Distinct") && e.getMessage().contains("does not exist")) {
				return null;
			} else {
				System.out.println(e.getMessage());
			}
		}
		
		return null;
	}
	
	public String getType(String columnName, String columnType, Integer numRows, Integer numDistinct ) throws SQLException {
	 
		if (columnType.contains("char") || columnType.contains("bit")) {
			if (numDistinct < 20 || numDistinct * 10 < numRows) {
				return "Categorical";
			} else {
				return "String"; //Geographic?
			}
		} else if (columnType.contains("time")) {
			return "Time";
		} else if (columnType.contains("date")) {
			return "Date";
		}
		//Ordinal?
		return "Numeric";
	}	
        
        public ResultSet GetStringData(String Att1,String Att2){
            	Statement stmt = null;
		ResultSet rs = null;
		try {
			// Get a statement from the connection
		    stmt = connection.createStatement() ;

		    // Execute the query
                    String query="Select "+Att1+","+Att2+" from "+table;
		    rs = stmt.executeQuery(query) ;
		}
		catch(Exception e)
		{
			System.out.println("Error in executing query: " + e.getMessage());
			e.printStackTrace();
		}
	return rs;
            }
        
        public void insertCorDims(String STmT){
            Statement st=null;
            try{
            //    System.out.println(STmT);
             st=  connection.createStatement();
             st.executeUpdate(STmT);
            }
            catch(Exception e)
		{
			System.out.println("Error in executing query: " + e.getMessage());
			e.printStackTrace();
		}
        }
        
        public void ExecuteQry(String STmT){
            Statement st=null;
            try{
            //    System.out.println(STmT);
             st=  connection.createStatement();
             st.executeUpdate(STmT);
             st.close();
           //  connection.close();
            }
            catch(Exception e)
		{
			System.out.println("Error in executing query: " + e.getMessage());
			e.printStackTrace();
		}
        }
        
        public ResultSet ExecuteQryWithRS(String STmT){
            	Statement stmt = null;
		ResultSet rs = null;
		try {
			// Get a statement from the connection
		    stmt = connection.createStatement() ;

		    // Execute the query
                   /// String query="Select "+Att1+","+Att2+" from "+table;
		    rs = stmt.executeQuery(STmT) ;
		}
		catch(Exception e)
		{
			System.out.println("Error in executing query: " + e.getMessage());
			e.printStackTrace();
		}
	return rs;
            }

}

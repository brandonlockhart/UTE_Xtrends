/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XTR_Fast;

//import InMem.*;
//import naiive.*;
//import XTR_Basic.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import metadata.Metadata;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author uqiibra2
 */
public class XTR_FastView {
    
    public enum XTR_FastMetric {PU_l2_norm,PU_Corr_distnace ,PI_Influence};
    public Metadata VMD=null;
    public String GrbyAtt="";
    public String AggAtt="";
    public String AggFun="";
    public String Table="";
    public double NormalizeV=0;
    public double TableSizeV=0;
    public String WhrCond="";
    public String Samplelimit="";
    public XTR_FastMetric CurrMetic=XTR_FastMetric.PU_l2_norm;
   
    public double TargetMean=0;
    // For Splitt_att
    public String MaxSpit_AttName="";
    public String MaxSpit_AttType="";
    public int MaxSpit_CountDistinct=0;
    // For Mergeing
    public String TopPredicate="";
    public double NearestTargetSim=0;
    //
    public boolean Print=true;
    //
    public DefaultTableModel lines= new DefaultTableModel();
    public DefaultTableModel OrgData= new DefaultTableModel();
    public DefaultTableModel DistinctAttData= new DefaultTableModel();
    public DefaultTableModel TableColumns= new DefaultTableModel();
    public DefaultTableModel AllSortedPredicates= new DefaultTableModel();
    public ArrayList<XTR_FastPredicate> ViewPredicates= new ArrayList<XTR_FastPredicate>();
    
    
    public void InializeView (String Query){
    
        try{
        String Whr="";
        String Table="";
        String Temp="";
        if(!Query.toLowerCase().contains("where")){
         Whr="";
         Table=Query.substring( Query.toLowerCase().lastIndexOf("from")+5, Query.toLowerCase().indexOf(" group by"));
         Temp=Query.toLowerCase();
        }
        else{
            
             Whr=Query.substring(Query.toLowerCase().indexOf("where ")+5 , Query.toLowerCase().indexOf(" group by"));
              Table=Query.substring( Query.toLowerCase().lastIndexOf("from")+5, Query.toLowerCase().indexOf(" where"));
              Temp=Query.substring(0,Query.toLowerCase().indexOf("where ")+5);
        }
        String GrpAtt=Temp.substring(Temp.toLowerCase().indexOf("select")+6,Temp.indexOf(","));
        
        Temp=Temp.substring(Temp.indexOf(",")+1,Temp.length());
        String AggAtt=Temp.substring(Temp.lastIndexOf("(")+1,Temp.lastIndexOf(")"));
        String AggFun=Temp.substring(0, Temp.lastIndexOf("("));
        
//        jLabel4.setText("Table:"+Table);
//        jLabel3.setText("Aggregated Att:"+AggFun+"("+AggAtt+")");
//        jLabel6.setText("Group By Att:"+GrpAtt);
//        jLabel8.setText("Where :"+Whr);
        //Setting up View
        this.Table=Table.replaceAll(" ", "");
        this.AggAtt=AggAtt.replaceAll(" ", "");
        this.GrbyAtt=GrpAtt.replaceAll(" ", "");
        this.AggFun=AggFun.replaceAll(" ", "");;
        this.WhrCond=Whr;
        if(!Whr.isEmpty()||Whr!="" && Whr.length()>5)
            this.TableSizeV=this.VMD.getNumRowsWhre(" where "+Whr);
        else
            this.TableSizeV=this.VMD.getNumRows();
        
        System.out.println("Start Inialtlization:.......");
        System.out.println("Table:"+Table.toUpperCase());
        System.out.println("Aggregated Att:"+AggFun.toUpperCase()+"("+AggAtt+")");
        System.out.println("Aggregated Function:"+AggFun.toUpperCase());
        System.out.println("Group By Att:"+GrpAtt);
        System.out.println("Where :"+Whr);
        System.out.println("Dataset Size: "+this.TableSizeV);    
        
       System.out.println("========= Explanation Attributes Metadata ============");
       System.out.println("========= Algorithm XTR_Fast ........... ============");
       System.out.println("======================================================");
       System.out.println("Column Name \t || \t Column Type \t || \t #Distinct");
       System.out.println("=======================================================");
       this.TableColumns=new DefaultTableModel();
       this.TableColumns=    GetTableColumns();
       
       String CurrentAtts=this.AggAtt+this.GrbyAtt+this.WhrCond;
       int MaxDistinct=0;
       
       for( int i=0; i<this.TableColumns.getRowCount();i++){
           
                String AttributeName=this.TableColumns.getValueAt(i, 0).toString();
                String AttributeType=this.TableColumns.getValueAt(i, 1).toString();
                int AttributeDitinct=Integer.parseInt(this.TableColumns.getValueAt(i, 2).toString());
                
           
                 if(! CurrentAtts.toLowerCase().contains(AttributeName.toLowerCase())){
                        System.out.println(AttributeName+" \t || \t "+AttributeType+ " \t || \t "+AttributeDitinct);
                        
                        if(AttributeDitinct>MaxDistinct){
                            MaxDistinct=AttributeDitinct;
                            this.MaxSpit_AttName=AttributeName;
                            this.MaxSpit_AttType=AttributeType;
                            this.MaxSpit_CountDistinct=AttributeDitinct;
                            
                        }
                 }
       }
       this.AllSortedPredicates= new DefaultTableModel();
             this.GetOrgData();
       System.out.println("=======================================================");
       System.out.println("Spliiting Attribbue >>>>>>>>>> \t"+this.MaxSpit_AttName);
       System.out.println("=======================================================");
       System.out.println("Finished Inialtlization:.......");
       System.out.println("=======================================================");
       
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
}
    
    public DefaultTableModel GetOrgData(){
        DefaultTableModel OrgData=new DefaultTableModel();
        try{
       // DefaultTableModel OrgData= new DefaultTableModel();
            String Stmt="";
//            if(this.WhrCond.length()>0)
//         Stmt=" select "+ this.AggAtt+"/"+NormalizeV+ " from "+ this.Table+" where "+this.WhrCond;
//            else
                 
                 
                     Stmt=" select "+ this.MaxSpit_AttName+ "  , "+this.GrbyAtt+","+this.AggAtt+" from "+ this.Table+" "+this.WhrCond;
                
        ResultSet rs=VMD.ExecuteQryWithRS(Stmt);
        
         OrgData=(DefaultTableModel) DbUtils.resultSetToTableModel(rs);
       // return OrgData;
                this.OrgData=OrgData;
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return OrgData;
    }
    
    public DefaultTableModel GetDistinctAttData( String Attr){
        DefaultTableModel OrgData=new DefaultTableModel();
        try{
       // DefaultTableModel OrgData= new DefaultTableModel();
            String Stmt="";
//            if(this.WhrCond.length()>0)
//         Stmt=" select "+ this.AggAtt+"/"+NormalizeV+ " from "+ this.Table+" where "+this.WhrCond;
//            else
                 
                 //if (Vsort>1)
                     Stmt=" select distinct "+ Attr+" from "+ this.Table+" "+this.WhrCond;
               //  else
                // Stmt=" select "+ this.AggAtt+"/"+NormalizeV+ " as AggAtt, "+Attr+" from "+ this.Table+" "+this.WhrCond+" order by AggAtt asc";
        ResultSet rs=VMD.ExecuteQryWithRS(Stmt);
        
         OrgData=(DefaultTableModel) DbUtils.resultSetToTableModel(rs);
       // return OrgData;
                this.DistinctAttData=OrgData;
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return OrgData;
    }
    
    public DefaultTableModel GetTableColumns(){
        DefaultTableModel Columns=new DefaultTableModel();
        String CurrentAtts=this.AggAtt+this.GrbyAtt+this.WhrCond;
        
         if(this.TableColumns.getRowCount()<=0){
             
                    this.TableColumns.addColumn("Attr_name");
                    this.TableColumns.addColumn("Att_Type");
                    this.TableColumns.addColumn("No_Distinct");
                    
         }
        
        try{
                
            Columns=VMD.getTableColumns();
            
             
            
            for(int i=0;i<Columns.getRowCount();i++)
            {
                String Colname=Columns.getValueAt(i, 0).toString();
                String ColType=Columns.getValueAt(i, 1).toString();
                int Distinct=0;
                 
                 if(! CurrentAtts.toLowerCase().contains(Colname.toLowerCase())) {
                     
                        if(!this.WhrCond.isEmpty()||this.WhrCond!="" && this.WhrCond.length()>5)
                            
                                Distinct=this.VMD.getNumDistinctWhr(Colname," where "+this.WhrCond);
                        else
                                Distinct=this.VMD.getNumDistinct(Colname);
                    
                        Vector PredAggvalues= new Vector();
                                        PredAggvalues.add(Colname);
                                        PredAggvalues.add(ColType);
                                        PredAggvalues.add(Distinct);
                                        
                                        
                        this.TableColumns.addRow(PredAggvalues);
                 
                 }
                
            }
        
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return this.TableColumns;
    }
    
    public Object[] GetDistinctCatData(String Attr){
        DefaultTableModel OrgData=GetDistinctAttData(  Attr);
        Object[] elements = new Object[OrgData.getRowCount()];
        try{
       
            for( int i=0;i<OrgData.getRowCount();i++){
                
                String Val=OrgData.getValueAt(i,0).toString();
                elements[i]=Val;
                
                
            
            }
        // return elements;
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return elements;
    }
    
    public void GetTargetAggMean(){
          
           // double rs=0;
      //  DefaultTableModel PredAggData=new DefaultTableModel();
        double Sum=0;
        for(int i=0;i<this.lines.getRowCount();i++){
            double vaul=Double.parseDouble(this.lines.getValueAt(i, 2).toString())/this.NormalizeV;
        Sum=Sum+vaul;
        }
         Sum=Sum/this.lines.getRowCount();
         this.TargetMean=Sum;
    }
    
    
     
     public DefaultTableModel GetFastPredAggData(String Predicate){
        
        DefaultTableModel PredAggData=new DefaultTableModel();
        //System.out.println("PredicateAggValue \t TargetAggValue");
         for(XTR_FastPredicate P : this.ViewPredicates){
             String Pwhre=P.PredicateWehe;
             if (Pwhre.equalsIgnoreCase(Predicate))
                 return P.PredAggData;
         }
       
          return PredAggData;
    }
     
     
     public XTR_FastPredicate GetFastPred(String Predicate){
        
       // DefaultTableModel PredAggData=new DefaultTableModel();
        //System.out.println("PredicateAggValue \t TargetAggValue");
         XTR_FastPredicate X=new XTR_FastPredicate();
         for(XTR_FastPredicate P : this.ViewPredicates){
             String Pwhre=P.PredicateWehe;
             if (Pwhre.equalsIgnoreCase(Predicate))
                 return P;
         }
       
          return X;
    }
}

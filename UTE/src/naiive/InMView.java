/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naiive;

//import InMem.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import metadata.Metadata;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author uqiibra2
 */
public class InMView {
    
    public enum Metrica {PU_l2_norm,PU_Corr_distnace ,PI_Influence};
    public Metadata VMD=null;
    public String GrbyAtt="";
    public String AggAtt="";
    public String AggFun="";
    public String Table="";
    public double NormalizeV=0;
    public double TableSizeV=0;
    public String WhrCond="";
    public String Samplelimit="";
    public Metrica CurrMetic=Metrica.PU_l2_norm;
   
    public double TargetMean=0;
    
    // For Mergeing
    public String TopPredicate="";
    public double NearestTargetSim=0;
    // For Splitting
     public double Split_ratio=0.2;
    //
    public boolean Print=false;
    //
    public DefaultTableModel lines= new DefaultTableModel();
    public DefaultTableModel OrgData= new DefaultTableModel();
    public DefaultTableModel DistinctAttData= new DefaultTableModel();
    public DefaultTableModel TableColumns= new DefaultTableModel();
    public DefaultTableModel AllSortedPredicates= new DefaultTableModel();
    public ArrayList<InNaivePredicate> ViewPredicates= new ArrayList<InNaivePredicate>();
    
    
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
            this.TableSizeV=this.VMD.getNumRowsWhre(" ");
        
        System.out.println("Start Inialtlization:.......");
        System.out.println("Table:"+Table.toUpperCase());
        System.out.println("Aggregated Att:"+AggFun.toUpperCase()+"("+AggAtt+")");
        System.out.println("Aggregated Function:"+AggFun.toUpperCase());
        System.out.println("Group By Att:"+GrpAtt);
        System.out.println("Where :"+Whr);
        System.out.println("Dataset Size: "+this.TableSizeV);    
        
       System.out.println("========= Explanation Attributes Metadata ===========");
       System.out.println("=================================");
       System.out.println("Column Name \t || \t Column Typle");
       System.out.println("=================================");
       this.TableColumns=GetTableColumns();
       
       String CurrentAtts=this.AggAtt+this.GrbyAtt+this.WhrCond;
       
       for( int i=0; i<this.TableColumns.getRowCount();i++){
                String AttributeName=this.TableColumns.getValueAt(i, 0).toString();
           // if(! AttributeName.toLowerCase().contains(CurrentAtts.toLowerCase()))
                 if(! CurrentAtts.toLowerCase().contains(AttributeName.toLowerCase()))
           System.out.println(this.TableColumns.getValueAt(i, 0).toString()+" \t || \t "+this.TableColumns.getValueAt(i, 1).toString() );
       }
       System.out.println("Finished Inialtlization:.......");
       
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
}
    
    public DefaultTableModel GetOrgData(double Vsort, String Attr){
        DefaultTableModel OrgData=new DefaultTableModel();
        try{
       // DefaultTableModel OrgData= new DefaultTableModel();
            String Stmt="";
//            if(this.WhrCond.length()>0)
//         Stmt=" select "+ this.AggAtt+"/"+NormalizeV+ " from "+ this.Table+" where "+this.WhrCond;
//            else
                 
                 if (Vsort>1)
                     Stmt=" select "+ this.AggAtt+ " as AggAtt, "+Attr+" from "+ this.Table+" "+this.WhrCond+" order by AggAtt desc";
                 else
                 Stmt=" select "+ this.AggAtt+ " as AggAtt, "+Attr+" from "+ this.Table+" "+this.WhrCond+" order by AggAtt asc";
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
        try{
      
        
            this.TableColumns=VMD.getTableColumns();
        
         //Columns=(DefaultTableModel) DbUtils.resultSetToTableModel(rs);
       // return Column names , Columns Type ;
              //  this.TableColumns=Columns;
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
         for(InNaivePredicate P : this.ViewPredicates){
             String Pwhre=P.PredicateWehe;
             if (Pwhre.equalsIgnoreCase(Predicate))
                 return P.PredAggData;
         }
       
          return PredAggData;
    }
     
     
     public InNaivePredicate GetFastPred(String Predicate){
        
       // DefaultTableModel PredAggData=new DefaultTableModel();
        //System.out.println("PredicateAggValue \t TargetAggValue");
         InNaivePredicate X=new InNaivePredicate();
         for(InNaivePredicate P : this.ViewPredicates){
             String Pwhre=P.PredicateWehe;
             if (Pwhre.equalsIgnoreCase(Predicate))
                 return P;
         }
       
          return X;
    }
}

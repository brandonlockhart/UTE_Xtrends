/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explelan;

import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import metadata.Metadata;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author uqiibra2
 */
public class View {
    public enum Metric {PU_l2_norm,PU_Corr_distnace ,PI_Influence};
    Metadata VMD=null;
    String GrbyAtt="";
    String AggAtt="";
    String AggFun="";
    String Table="";
    double NormalizeV=0;
    double TableSizeV=0;
    String WhrCond="";
    String Samplelimit="";
    Metric CurrMetic=Metric.PU_l2_norm;
    double CurrentTargetSim=0;
    double TargetMean=0;
    double LowerSimlirity=0;
    String CurrentNearPredicate="";
    DefaultTableModel lines= new DefaultTableModel();
    DefaultTableModel OrgData= new DefaultTableModel();
    
    public DefaultTableModel GetOrgData(){
        DefaultTableModel OrgData=new DefaultTableModel();
        try{
       // DefaultTableModel OrgData= new DefaultTableModel();
            String Stmt="";
//            if(this.WhrCond.length()>0)
//         Stmt=" select "+ this.AggAtt+"/"+NormalizeV+ " from "+ this.Table+" where "+this.WhrCond;
//            else
                 Stmt=" select "+ this.AggAtt+"/"+NormalizeV+ " from "+ this.Table+" "+this.WhrCond;
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
    
     public void GetLowerTragetSimilirity(){
        
        //DefaultTableModel PredAggData=GetPredAggData();
        //System.out.println("PredicateAggValue \t TargetAggValue");
        double utility = 0;
            for(int i=0;i<this.lines.getRowCount();i++){
                double resTargetgvalue=Double.parseDouble( this.lines.getValueAt(i, 2).toString())/this.NormalizeV;
                double resPredcvalue=0;
               
               // System.out.println(resPredcvalue+"\t"+resTargetgvalue);
                    utility += Math.pow(resTargetgvalue-resPredcvalue,2);
                
                
            }
            utility = Math.sqrt(utility);
            System.out.println(utility);
             this.LowerSimlirity=utility;
        //  return utility;
    }
}

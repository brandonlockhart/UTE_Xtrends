/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explelan;

import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import metadata.Metadata;
import net.proteanit.sql.DbUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

/**
 *
 * @author uqiibra2
 */
public class Predicate {
    String PredName="";
    String PredicateWehe="";
    double NoRecods=0;
    double ReductionFactor=0;
    double InternalSim=0;
    double TargetSim=0;
    double ValueAfterDel=0;
    double Influence=0;
   
    DefaultTableModel PredAggData=new DefaultTableModel ();
    DefaultTableModel PredOrgData=new DefaultTableModel ();
    View View;
    Metadata M=null;
    
    public void ComputeInternalSimilirity(){
        
        DefaultTableModel PredicateOrgData=GetPredOrgData();
       // System.out.println("PredicateOrgValue \t CurrOrgValue");
        double utility = 0;
            for(int i=0;i<this.View.OrgData.getRowCount();i++){
                double resOrgvalue=Double.parseDouble( this.View.OrgData.getValueAt(i, 0).toString());
                double resPredcvalue=0;
                if (i<PredicateOrgData.getRowCount()) resPredcvalue=Double.parseDouble( PredicateOrgData.getValueAt(i, 0).toString());
                else  resPredcvalue=0;
                    //resPredcvalue=0;
                  // System.out.println(resPredcvalue+"\t"+resOrgvalue);
                    utility += Math.pow(resOrgvalue-resPredcvalue,2);
                
                
            }
            utility = Math.sqrt(utility);
            this.InternalSim=utility;
         // return utility;
    }
    
    public void ComputeTragetSimilirity(){
        
        DefaultTableModel PredAggData=GetPredAggData();
        //System.out.println("PredicateAggValue \t TargetAggValue");
        double utility = 0;
            for(int i=0;i<this.View.lines.getRowCount();i++){
                double resTargetgvalue=Double.parseDouble( this.View.lines.getValueAt(i, 2).toString())/this.View.NormalizeV;
                double resPredcvalue=0;
                if (i<PredAggData.getRowCount()) resPredcvalue=Double.parseDouble( PredAggData.getValueAt(i, 1).toString());
                else  resPredcvalue=0;
               // System.out.println(resPredcvalue+"\t"+resTargetgvalue);
                    utility += Math.pow(resTargetgvalue-resPredcvalue,2);
                
                
            }
            utility = Math.sqrt(utility);
             this.TargetSim=utility;
        //  return utility;
    }
    
    public void ComputeTragetPCorrDis(){
        
        DefaultTableModel PredAggData=GetPredAggData();
        //System.out.println("PredicateAggValue \t TargetAggValue");
        double PredAggDataMean=GetPredAggMean();
         double diffVline=0;
         double diffPredv=0;
         double Cov=0;
        double StdVline = 0;
        double StdPred = 0;
            for(int i=0;i<this.View.lines.getRowCount();i++){
                double VLine=Double.parseDouble( this.View.lines.getValueAt(i, 2).toString())/this.View.NormalizeV;
                double resPredcvalue=0;
                if (i<PredAggData.getRowCount()) resPredcvalue=Double.parseDouble( PredAggData.getValueAt(i, 1).toString());
                else  resPredcvalue=0;
               // System.out.println(resPredcvalue+"\t"+resTargetgvalue);
               diffVline=VLine-this.View.TargetMean;
               diffPredv=resPredcvalue-PredAggDataMean;
               Cov=Cov+ (diffVline*diffPredv);
                  StdVline=StdVline+Math.pow(diffVline, 2);
                  StdPred=StdPred+Math.pow(diffPredv, 2);
                
            }
            StdVline = Math.sqrt(StdVline);
            StdPred = Math.sqrt(StdPred);
            Cov=Cov/(this.View.lines.getRowCount()-1);
            double Corr= Cov/StdVline*StdPred;
             this.TargetSim=Corr;
        //  return utility;
    }
    
    public void ComputeTragetCorr(){
        
        DefaultTableModel PredAggData=GetPredAggData();
        //System.out.println("PredicateAggValue \t TargetAggValue");
        
        double [] vline= new double[this.View.lines.getRowCount()];
        double [] vPred= new double[this.View.lines.getRowCount()];
            for(int i=0;i<this.View.lines.getRowCount();i++){
                double VLine=Double.parseDouble( this.View.lines.getValueAt(i, 2).toString())/this.View.NormalizeV;
                vline[i]=VLine;
                double resPredcvalue=0;
                if (i<PredAggData.getRowCount()) resPredcvalue=Double.parseDouble( PredAggData.getValueAt(i, 1).toString());
                else  resPredcvalue=0;
                vPred[i]=resPredcvalue;
               
                
            }
//            Covariance Cov= new Covariance();
//            Variance Var= new Variance();
//            double s=Cov.covariance(vline, vPred);
//            double varLine=Var.evaluate(vline);
//            double varPred=Var.evaluate(vPred);
//            double cr= s/Math.sqrt(varLine)*Math.sqrt(varPred);
//              this.TargetSim=1-cr;
            PearsonsCorrelation p= new PearsonsCorrelation();
            
            double Corr= p.correlation(vline, vPred);
             this.TargetSim=1-Corr;
           
        //  return utility;
    }
  
    public void ComputeInflunce() {
      
          double utility = 0;
           double MaxInf = 0;
           double PredNoRec=this.NoRecods;
           
            for(int i=0;i<this.PredAggData.getRowCount();i++){
                String PreDResultIdName=this.PredAggData.getValueAt(i, 0).toString();
                double PredCurrV=Double.parseDouble(this.PredAggData.getValueAt(i, 1).toString());
                
                 for(int j=0;j<this.View.lines.getRowCount();j++){
                     String ResultIdName=this.View.lines.getValueAt(i, 0).toString();
                     double ActualV=Double.parseDouble(this.View.lines.getValueAt(j, 1).toString())/this.View.NormalizeV;
                
                if ( ResultIdName.equalsIgnoreCase(PreDResultIdName)){
                    utility =(ActualV-PredCurrV)/PredNoRec;
                    //
                    
               //vectorr
                if((PredCurrV/ActualV)>1) utility=utility*-1;
               else if((PredCurrV/ActualV)<1) utility=utility*1;
               else if((PredCurrV/ActualV)==1) utility=utility*0;
                
                if(utility>MaxInf) MaxInf= utility;
                }
                 }
            }
           // utility = Math.sqrt(utility);
             this.Influence=MaxInf;
         // return MaxInf;
      }
    
    public DefaultTableModel GetPredOrgData(){
        
        DefaultTableModel OrgData=new DefaultTableModel();
        try{
       // DefaultTableModel OrgData= new DefaultTableModel();
            String Stmt="";
            if(View.WhrCond.length()>0)
             Stmt=" select "+ View.AggAtt+"/"+View.NormalizeV+ " from "+ View.Table+" "+View.WhrCond+this.PredicateWehe;
            else
                Stmt=" select "+ View.AggAtt+"/"+View.NormalizeV+ " from "+ View.Table+" where "+this.PredicateWehe.substring(this.PredicateWehe.indexOf("and ")+4, this.PredicateWehe.length());
            
        ResultSet rs=M.ExecuteQryWithRS(Stmt);
         OrgData=(DefaultTableModel) DbUtils.resultSetToTableModel(rs);
         
         this.PredOrgData=OrgData;
         this.NoRecods=OrgData.getRowCount();
         this.ReductionFactor=this.NoRecods/View.OrgData.getRowCount();
       // return OrgData;
                
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return OrgData;
    }
    
    public DefaultTableModel GetPredAggData(){
        
        DefaultTableModel PredAggData=new DefaultTableModel();
        try{
       // DefaultTableModel OrgData= new DefaultTableModel();
           
            String Stmt="";
            if(View.WhrCond.length()>0)
                Stmt=" select "+View.GrbyAtt +","+View.AggFun+"("+ View.AggAtt+")/"+View.NormalizeV+ " from "+ View.Table+" "+this.PredicateWehe+" group by "+View.GrbyAtt;
            else
               Stmt=" select "+View.GrbyAtt +","+View.AggFun+"("+ View.AggAtt+")/"+View.NormalizeV+ " from "+ View.Table+" "+" where "+
                        this.PredicateWehe.substring(this.PredicateWehe.indexOf("and ")+4, this.PredicateWehe.length())+ " group by "+View.GrbyAtt;
            
           // System.out.println(Stmt);
        ResultSet rs=M.ExecuteQryWithRS(Stmt);
         PredAggData=(DefaultTableModel) DbUtils.resultSetToTableModel(rs);
       // return OrgData;
                this.PredAggData=PredAggData;
//                this.ValueAfterDel=Double.parseDouble(PredAggData.getValueAt(0, 1).toString());
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return PredAggData;
    }
    
       
    public double GetPredNoRecords(){
          
           // double rs=0;
      //  DefaultTableModel PredAggData=new DefaultTableModel();
        try{
        //rs=M.getNumRowsWhre(this.PredicateWehe);
        //this.NoRecods=rs;
                }
        catch(Exception e){
                e.printStackTrace();
                }
        return this.NoRecods;
    }
    
    public double GetPredAggMean(){
          
           // double rs=0;
      //  DefaultTableModel PredAggData=new DefaultTableModel();
        double Sum=0;
        for(int i=0;i<this.PredAggData.getRowCount();i++){
            double vaul=Double.parseDouble(this.PredAggData.getValueAt(i, 1).toString());
        Sum=Sum+vaul;
        }
        return Sum=Sum/this.PredAggData.getRowCount();
    }
    
    public void PredPrint(){
         System.out.println(this.PredName+"\t"+this.PredicateWehe+"\t"+this.NoRecods+"\t"+this.ValueAfterDel+"\t"+this.Influence+"\t"+this.InternalSim+"\t"+this.TargetSim);
    }
    
    public void PredAddinfo(DefaultTableModel PredMergedInfoModel ){
         
        Vector PredStatInf= new Vector();
        
                                        PredStatInf.add(this.PredName);
                                        PredStatInf.add(this.PredicateWehe);
                                        PredStatInf.add(this.NoRecods);
                                        PredStatInf.add(this.ValueAfterDel);
                                        PredStatInf.add(this.Influence);
                                        PredStatInf.add(this.InternalSim);
                                        PredStatInf.add(this.TargetSim);
        
                                        PredMergedInfoModel.addRow(PredStatInf);
       //  System.out.println(this.PredName+"\t"+this.PredicateWehe+"\t"+this.NoRecods+"\t"+this.ValueAfterDel+"\t"+this.Influence+"\t"+this.InternalSim+"\t"+this.TargetSim);
    }
}

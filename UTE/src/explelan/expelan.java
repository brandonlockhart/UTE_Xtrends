/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explelan;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import metadata.Metadata;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author uqiibra2
 */
public class expelan {
    /*
      public void  SearchResid( DefaultTableModel m ,String ResPredicate, Metadata mg,double NormalizeV,double Currdist, DefaultTableModel lines) {
		
		
        try{
         //loop all tuples in result id data
          int idsidx=0;
          int AttValidx=0;
         //Predicates Data
          
                 DefaultTableModel model = new DefaultTableModel();		
             
                 model.addColumn("ResultID");
                 model.addColumn("Predoicate");
                 model.addColumn("Valueafterdelete");
                 model.addColumn("Influnce");
                 model.addColumn("INernaldistance");
                 model.addColumn("CurrDistanceToTarget");
                 model.addColumn("linesDistance");
           
          
			double Attmax=mg.getMax("Temp", "");
                        
                    //Normalize to Max
                    for(int i=0;i<m.getColumnCount();i++){
                        if(m.getColumnName(i).equalsIgnoreCase("temp"))
                        { m= normalize(m,i,NormalizeV); 
                        AttValidx=i;}
                         if (m.getColumnName(i).equalsIgnoreCase("ids")) idsidx=i;
                    }
              System.out.println("Predicate \t Valueafterdelete \t Influnce \t INterdistance \t CurrDistanceToTarget linesDistance");
           
           // Evualte Predicates
            for(int j=0;j<m.getRowCount();j++){
                String ids=m.getValueAt(j, idsidx).toString();
                String CurrPredicate=" "+ResPredicate +" and ids<>"+ids ;
               // String Atrr="ids";
                //No of Records in Predicate
                double noofRec=mg.getNumRowsWhre("where"+CurrPredicate);
                System.out.print(CurrPredicate+"\t");
                String TempSql="select temp from examp where "+ CurrPredicate;
                double CurrPredicateAVG=mg.getAVG("temp", " where "+CurrPredicate)/NormalizeV;
                System.out.print(CurrPredicateAVG+"\t");
                // Compute influnce 
                double PredInf= ComputeInflunce( lines ,  CurrPredicateAVG, ResPredicate, noofRec);
                System.out.print(PredInf+"\t");
                //Compute Current to targetline distance 
                double PrdTargtDist=ComputeDistancePredtoTarget( lines , CurrPredicateAVG,ResPredicate);
                //System.out.print("\t"+PrdTargtDist);
                
                ResultSet Temprs=mg.ExecuteQryWithRS(TempSql);
                DefaultTableModel Tempm= (DefaultTableModel) DbUtils.resultSetToTableModel(Temprs);
               
               
                        
                    //Normalize to Max
                    for(int i=0;i<Tempm.getColumnCount();i++){
                        if(Tempm.getColumnName(i).equalsIgnoreCase("temp"))
                           Tempm= normalize(Tempm,i,NormalizeV);
                             }
                    
                   //Compute Similirity
                    double dist=ComputeDistance2(m,Tempm,AttValidx);
                    Vector textData = new Vector();
                                textData.add(ResPredicate);
                                textData.add(CurrPredicate);
                                textData.add(CurrPredicateAVG);
                                textData.add(PredInf);
                                textData.add(dist);
                                textData.add(PrdTargtDist);
                                textData.add(Currdist);
                                 model.addRow(textData );
                //    double diffdist=Currdist-dist;
		System.out.print("\t"+dist+"\t"+ PrdTargtDist +Currdist+"\t");
                System.out.println();
            }
        
         // Merge Predicates 
            MergePred Mrgr=new MergePred();
            Mrgr.Merger(model,mg, NormalizeV, lines, m, AttValidx);
         
        }
        catch (Exception e){
        e.printStackTrace();
        }
      
	
	}
      
     */
      
      public double ComputeDistancePredtoTarget(DefaultTableModel lines , double PredicateVal,String Resultid) {
      
          double utility = 0;
            for(int i=0;i<lines.getRowCount();i++){
                String ResultIdName=lines.getValueAt(i, 0).toString();
                
                double targetV=Double.parseDouble(lines.getValueAt(i, 2).toString());
                if ( Resultid.contains(ResultIdName))
                    utility += Math.pow(targetV-(PredicateVal),2);
                else
                    utility += Math.pow(targetV-targetV,2);
                
            }
            utility = Math.sqrt(utility);
          return utility;
      }
      
      public double ComputeInflunce(DefaultTableModel lines , double PredicateVal,String Resultid,double noofRec) {
      
          double utility = 0;
            for(int i=0;i<lines.getRowCount();i++){
                String ResultIdName=lines.getValueAt(i, 0).toString();
                
                double ActualV=Double.parseDouble(lines.getValueAt(i, 1).toString());
                double TargetV=Double.parseDouble(lines.getValueAt(i, 2).toString());
                
                if ( Resultid.contains(ResultIdName)){
                    utility =(ActualV-PredicateVal)/noofRec;
               //vectorr
                if((TargetV/ActualV)>1) utility=utility*-1;
               else if((TargetV/ActualV)<1) utility=utility*1;
               else if((TargetV/ActualV)==1) utility=utility*0;
                }
            }
           // utility = Math.sqrt(utility);
          return utility;
      }
      
       public double ComputeDistance2(DefaultTableModel ResOrgData ,DefaultTableModel ResPredicateData,int iDx ) {
      
          double utility = 0;
            for(int i=0;i<ResOrgData.getRowCount();i++){
                double resOrgvalue=Double.parseDouble( ResOrgData.getValueAt(i, iDx).toString());
                double resPredcvalue=0;
                if (i>=ResPredicateData.getRowCount()) resPredcvalue=0;
                else resPredcvalue=Double.parseDouble( ResPredicateData.getValueAt(i, 0).toString());
                
                    utility += Math.pow(resOrgvalue-resPredcvalue,2);
                
                
            }
            utility = Math.sqrt(utility);
          return utility;
      }
       
       public double ActuaTarDistance2(DefaultTableModel ResOrgData ) {
      
          double utility = 0;
            for(int i=0;i<ResOrgData.getRowCount();i++){
                double Orgvalue=Double.parseDouble( ResOrgData.getValueAt(i, 1).toString());
                double Targetvalue=Double.parseDouble( ResOrgData.getValueAt(i, 2).toString());;
                
                
                    utility += Math.pow(Orgvalue-Targetvalue,2);
                
                
            }
            utility = Math.sqrt(utility);
          return utility;
      }
      
      public String GetMeasureRange(DefaultTableModel Data, int colIdx){
          
          double min=Double.parseDouble( Data.getValueAt(0, colIdx).toString());
          double max=0;
          for(int i=0; i<Data.getRowCount();i++){
              double val=Double.parseDouble( Data.getValueAt(i, colIdx).toString());
              if(min>=val) min=val;
              if(max<=val) max=val;
          }
          String Predicate=Data.getColumnName(colIdx) +" between " +min+" and "+max; 
          System.out.println(Predicate);
          return Predicate;
        }
      
      public DefaultTableModel normalize(DefaultTableModel Data, int colIdx,double value){
          for(int i=0;i<Data.getRowCount();i++){
              double CurV= Double.parseDouble( Data.getValueAt(i, colIdx).toString() );
              double norV=CurV/value;
              Data.setValueAt(norV, i, colIdx);
          }
      return Data;
      }
    
}

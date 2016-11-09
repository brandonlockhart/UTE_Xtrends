/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explelan;

/**
 *
 * @author uqiibra2
 */
import XTR_Basic.XTR_BaiscView;
import XTR_Fast.XTR_FastView;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import naiive.InMView;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.CategoryPointerAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;


public class Dualchart  implements ChartMouseListener  {
    
        public JFreeChart chart;
        public ChartPanel chartPanel;
        public DefaultTableModel model;
       public Dualchart (DefaultTableModel m ){
        //  super(m);
       DrawTarget(m);
        this.model=m;
//       chart2.addProgressListener(new ChartProgressListener() {
//
//        @Override
//        public void chartProgress(ChartProgressEvent cpe) {
//        if(cpe.getType()==ChartProgressEvent.DRAWING_FINISHED){
//         System.out.println("Click event!");
//                }
//        }
//        });
    
        //ChartPanel chartPanel = new ChartPanel(chart2, false);
  
      
      
    }
    
    
    public  void DrawTarget(DefaultTableModel m){
        try{
        String dim=m.getColumnName(0).toString();
        String measure=m.getColumnName(1).toString();;
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        NumberFormat nf = NumberFormat.getInstance();
        for(int i=0;i<m.getRowCount();i++){
            String attr=m.getValueAt(i, 0).toString();
            
           // double val= Double.parseDouble( m.getValueAt(i, 1).toString());
             double val= nf.parse(m.getValueAt(i, 1).toString()).doubleValue();
           // double Tval=Double.parseDouble( m.getValueAt(i, 2).toString());
              double Tval=nf.parse(m.getValueAt(i, 2).toString()).doubleValue();
             dataset1.addValue(val,"Results",attr);
             dataset2.addValue(Tval,"Expected",attr);
        }
        
            CategoryItemRenderer renderer = new BarRenderer();
            renderer.setItemLabelsVisible(true);
       
        CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);
        plot.setDomainAxis(new CategoryAxis(dim));
        plot.setRangeAxis(new NumberAxis(measure));
        
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        
        CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        this.chart = new JFreeChart(plot);
        this.chart.setTitle(" Bar Chart");
        
          chartPanel = new ChartPanel(this.chart,false);
          chartPanel.addChartMouseListener(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
   
     // return chart;
        
           
                       
}
    
    public  void addnewline(String DS,DefaultTableModel m, View v){
        try{
        String dim=m.getColumnName(0).toString();
        String measure=m.getColumnName(1).toString();;
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
       // DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        NumberFormat nf = NumberFormat.getInstance();
        for(int i=0;i<m.getRowCount();i++){
            String attr=m.getValueAt(i, 0).toString();
            
           //
             double val= nf.parse(m.getValueAt(i, 1).toString()).doubleValue()*v.NormalizeV;
            dataset1.addValue(val,DS,attr);
            
        }
        
       
        CategoryPlot plot = this.chart.getCategoryPlot();
        int datasetCount=plot.getCategories().size()-1;
         plot.setDomainAxis(new CategoryAxis(v.GrbyAtt));
         plot.setRangeAxis(new NumberAxis(v.AggFun+"("+v.AggAtt+")"));
        //plot.setDataset(dataset1);
        //plot.setRenderer(renderer);
       
        
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        
        CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        plot.setDataset(datasetCount, dataset1);
        plot.setRenderer(datasetCount, renderer2);
        
        
     //   plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        this.chart = new JFreeChart(plot);
        this.chart.setTitle(" Bar Chart");
   
          chartPanel = new ChartPanel(this.chart,false);
          chartPanel.addChartMouseListener(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
   
     // return chart;
        
           
                       
}
    
    public  void addnewlineNaiive(String DS,DefaultTableModel m, InMView v){
        try{
        String dim=m.getColumnName(0).toString();
        String measure=m.getColumnName(1).toString();;
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
       // DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        NumberFormat nf = NumberFormat.getInstance();
        for(int i=0;i<m.getRowCount();i++){
            String attr=m.getValueAt(i, 0).toString();
            
           //
             double val= nf.parse(m.getValueAt(i, 1).toString()).doubleValue()*v.NormalizeV;
            dataset1.addValue(val,DS,attr);
            
        }
        
       
        CategoryPlot plot = this.chart.getCategoryPlot();
        int datasetCount=plot.getCategories().size()-1;
         plot.setDomainAxis(new CategoryAxis(v.GrbyAtt));
         plot.setRangeAxis(new NumberAxis(v.AggFun+"("+v.AggAtt+")"));
        //plot.setDataset(dataset1);
        //plot.setRenderer(renderer);
       
        
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        
        CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        plot.setDataset(datasetCount, dataset1);
        plot.setRenderer(datasetCount, renderer2);
        
        
     //   plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        this.chart = new JFreeChart(plot);
        this.chart.setTitle(" Bar Chart");
   
          chartPanel = new ChartPanel(this.chart,false);
          chartPanel.addChartMouseListener(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
   
     // return chart;
        
           
                       
}
    
    public  void addnewBarNaiive(String DS,DefaultTableModel m, InMView v){
        try{
        String dim=m.getColumnName(0).toString();
        String measure=m.getColumnName(1).toString();;
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
       // DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        NumberFormat nf = NumberFormat.getInstance();
        for(int i=0;i<m.getRowCount();i++){
            String attr=m.getValueAt(i, 0).toString();
            
           //
             double val= nf.parse(m.getValueAt(i, 1).toString()).doubleValue()*v.NormalizeV;
            dataset1.addValue(val,DS,attr);
            
        }
        
       
        CategoryPlot plot = this.chart.getCategoryPlot();
        int datasetCount=plot.getCategories().size()-1;
         plot.setDomainAxis(new CategoryAxis(v.GrbyAtt));
         plot.setRangeAxis(new NumberAxis(v.AggFun+"("+v.AggAtt+")"));
        //plot.setDataset(dataset1);
        //plot.setRenderer(renderer);
       
        
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        
        CategoryItemRenderer renderer2 = new BarRenderer();
//        plot.setDataset(dataset1);
//        plot.setRenderer( renderer2);
        
        plot.setDataset(datasetCount,dataset1);
        plot.setRenderer(datasetCount, renderer2);
        
        
     //   plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        this.chart = new JFreeChart(plot);
        this.chart.setTitle(" Bar Chart");
   
          chartPanel = new ChartPanel(this.chart,false);
          chartPanel.addChartMouseListener(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
   
     // return chart;
        
           
                       
}
    
    
    public  void addnewlineXTR_BasicView(String DS,DefaultTableModel m, XTR_BaiscView v){
        try{
        String dim=m.getColumnName(0).toString();
        String measure=m.getColumnName(1).toString();;
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
       // DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        NumberFormat nf = NumberFormat.getInstance();
        for(int i=0;i<m.getRowCount();i++){
            String attr=m.getValueAt(i, 0).toString();
            
           //
             double val= nf.parse(m.getValueAt(i, 1).toString()).doubleValue()*v.NormalizeV;
            dataset1.addValue(val,DS,attr);
            
        }
        
       
        CategoryPlot plot = this.chart.getCategoryPlot();
        int datasetCount=plot.getCategories().size()-1;
         plot.setDomainAxis(new CategoryAxis(v.GrbyAtt));
         plot.setRangeAxis(new NumberAxis(v.AggFun+"("+v.AggAtt+")"));
        //plot.setDataset(dataset1);
        //plot.setRenderer(renderer);
       
        
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        
        CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        plot.setDataset(datasetCount, dataset1);
        plot.setRenderer(datasetCount, renderer2);
        
        
     //   plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        this.chart = new JFreeChart(plot);
        this.chart.setTitle(" Bar Chart");
   
          chartPanel = new ChartPanel(this.chart,false);
          chartPanel.addChartMouseListener(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
   
     // return chart;
        
           
                       
}
    
     public  void addnewlineXTR_FastView(String DS,DefaultTableModel m, XTR_FastView v){
        try{
        String dim=m.getColumnName(0).toString();
        String measure=m.getColumnName(1).toString();;
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
       // DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        NumberFormat nf = NumberFormat.getInstance();
        for(int i=0;i<m.getRowCount();i++){
            String attr=m.getValueAt(i, 0).toString();
            
           //
             double val= nf.parse(m.getValueAt(i, 1).toString()).doubleValue()*v.NormalizeV;
            dataset1.addValue(val,DS,attr);
            
        }
        
       
        CategoryPlot plot = this.chart.getCategoryPlot();
        int datasetCount=plot.getCategories().size()-1;
         plot.setDomainAxis(new CategoryAxis(v.GrbyAtt));
         plot.setRangeAxis(new NumberAxis(v.AggFun+"("+v.AggAtt+")"));
        //plot.setDataset(dataset1);
        //plot.setRenderer(renderer);
       
        
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        
        CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        plot.setDataset(datasetCount, dataset1);
        plot.setRenderer(datasetCount, renderer2);
        
        
     //   plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        this.chart = new JFreeChart(plot);
        this.chart.setTitle(" Bar Chart");
   
          chartPanel = new ChartPanel(this.chart,false);
          chartPanel.addChartMouseListener(this);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
   
     // return chart;
        
           
                       
}
    
    public void UpdateM(String Cat, double Val){
        for(int i=0;i<this.model.getRowCount();i++){
            String attr=this.model.getValueAt(i, 0).toString();
            if(attr.equalsIgnoreCase(Cat)) {
                this.model.setValueAt(Val, i, 2);
                this.model.fireTableDataChanged();
                break;
            }
            
        }
        
    }
   

   // @Override
    public void chartMouseClicked(ChartMouseEvent event) {
        int x = event.getTrigger().getX();
        int y = event.getTrigger().getY();
        CategoryPlot plot = this.chart.getCategoryPlot();    
        
        ValueAxis axis = plot.getRangeAxis();
            //Rectangle2D area =chartPanel.getScreenDataArea();
      //  java.awt.geom.Rectangle2D area = this.chartPanel.getScreenDataArea();
        
         // Rectangle2D area1 =this.chartPanel.getChartRenderingInfo().getPlotInfo().getPlotArea();
            Rectangle2D area2 =this.chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();
            
            Point2D p = this.chartPanel.translateScreenToJava2D( event.getTrigger().getPoint());
            double maxy=area2.getMaxY();
            double miny=area2.getMinY();
            double maxv=axis.getUpperBound();
           double vv= maxv*(maxy-p.getY())/(maxy-miny);
//            RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();
//          
           int categCount=plot.getCategories().size();
           double maxx=area2.getMaxX();
           double minx=area2.getMinX();
           double catlenght= (maxx-minx)/categCount;
           double selectCatIdx=(p.getX()-minx)/catlenght;
          // int SelCatIdx=(int) selectCatIdx;
           List  SlecCat=plot.getCategories();
           int i=(int) Math.round(selectCatIdx);
           String SCat="";
          
           if (i>0)
            SCat= SlecCat.get((i-1) ).toString();
           
                   
                   //        = plot.getDomainAxis().getCategoryJava2DCoordinate(CategoryAnchor.START, (int) selectCatIdx, categCount, area, RectangleEdge.BOTTOM);
                     //      plot.getDomainAxis().get        
                           //getCategoryJava2DCoordinate(CategoryAnchor.START, y, y, area, RectangleEdge.TOP)
            System.out.println("Y Value app " +vv+" X value "+SCat);
            vv=new BigDecimal(vv).setScale(2,RoundingMode.HALF_UP).doubleValue();
                  //w  vv=Math.round(vv);
            
            this.chartPanel.setDisplayToolTips(true);
            this.chartPanel.setToolTipText(SCat+","+vv);
            CategoryPointerAnnotation categorypointerannotation = new CategoryPointerAnnotation("E("+SCat+",\n "+vv+")",SCat,vv,90);
            categorypointerannotation.setPaint(Color.ORANGE);
            categorypointerannotation.setFont(new Font("SansSerif", 0, 12));
            categorypointerannotation.setTextAnchor(TextAnchor.BOTTOM_RIGHT);
            
            
            plot.addAnnotation(categorypointerannotation);
            UpdateM( SCat, vv);
            
           //double yCoordinate1= DatasetUtilities.findYValue(plot.getDataset(), 1, yCoordinate);
              // double chartY = rangeAxis.java2DToValue(p.getY(), plotArea,    rangeAxisEdge);
    }

   // @Override
    public void chartMouseExited (ChartMouseEvent event){
        DrawTarget(this.model);
    }
    
    public void chartMouseMoved(ChartMouseEvent event) {
        
        
////        int x = event.getTrigger().getX();
//        int y = event.getTrigger().getY();
//        ChartEntity entity = event.getEntity();
//        
//        if (entity != null) {
//            System.out.println("Mouse moved: " + x + ", " + y + ": " + entity.toString());
//            if(entity instanceof XYItemEntity){
//            	XYItemEntity xYItemEntity = (XYItemEntity)entity;
//            	
//            	XYPlot plot = (XYPlot) this.chart.getPlot();
//                XYItemRenderer renderer = plot.getRenderer();
//                XYDataset dataset = plot.getDataset();
//                
//                int index = xYItemEntity.getSeriesIndex();
//                System.out.println("Index::" + index);
//                
//                for (int i = 0; i < dataset.getSeriesCount(); i++) {
//                    renderer.setSeriesStroke(i, new BasicStroke(1.0f));
//                    if (index==i) {
//                        renderer.setSeriesStroke(i, new BasicStroke(2.0f));
//                    }
//                    chartPanel.getGraphics().drawOval(x-5, y-5, 10, 10);
//                }
//            }
//        }
//        else {
//            System.out.println(
//                "Mouse moved: " + x + ", " + y + ": null entity.");
//        }
      //  System.out.println("Item ");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   
     
    
    
}
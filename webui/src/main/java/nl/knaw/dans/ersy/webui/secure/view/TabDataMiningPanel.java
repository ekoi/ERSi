package nl.knaw.dans.ersy.webui.secure.view;

import nl.knaw.dans.ersi.config.CanopyConfig;
import nl.knaw.dans.ersi.config.ClusterAlgorithmConfig;
import nl.knaw.dans.ersi.config.ClusteringConfig;
import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.KMeansConfig;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus.ProcessName;
import nl.knaw.dans.ersy.textmining.clustering.utils.DataClusteringExecutor;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author akmi
 * 
 */
public class TabDataMiningPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1973574682018245001L;

	public TabDataMiningPanel(String id, String ersyHome) {
		super(id);
		
		final ProcessStatus ps = new ProcessStatus(ProcessName.DATA_MINING, ersyHome);
		
		boolean executeIsAllowed = !ps.isRunning();
		if (executeIsAllowed) {
			final ProcessStatus psde = new ProcessStatus(ProcessName.DATA_CLEANING, ersyHome);
			executeIsAllowed = !psde.isRunning();
		}
		final boolean dataMiningIsAllowed = executeIsAllowed;
		
		add (new Label("currentStatus", new Model<String>(ps.giveCurrentStatus())));
		add (new Label("lastStatus", new Model<String>(ps.giveTimeLastProcess())));
		
		ConfigurationReader confReader = new ConfigurationReader(ersyHome);
		ClusteringConfig dcc = confReader.getClusteringConfig();
		ClusterAlgorithmConfig cac = dcc.getClusterAlgorithmConfig();
		CanopyConfig c = cac.getCanopyConfig();
		KMeansConfig k = cac.getkMeansConfig();
		
		
		add (new Label("inputPath", new Model<String>(dcc.getInputVectorsPath())));
		add (new Label("outputPath", new Model<String>(dcc.getOutputPath())));
		
		add (new Label("canopyDistanceMeasure", new Model<String>(c.getDistanceMeasureClassName())));
		add (new Label("canopyDmT1", new Model<Double>(c.getDistanceMetricT1())));
		add (new Label("canopyDmT2", new Model<Double>(c.getDistanceMetricT2())));
		add (new Label("canopyTreshold", new Model<Double>(c.getClusterClassificationThreshold())));
		
		add (new Label("kmeansDistanceMeasure", new Model<String>(k.getDistanceMeasureClassName())));
		add (new Label("kmeandConvDelta", new Model<Double>(k.getConvergenceDelta())));
		add (new Label("kmeansMaxIteration", new Model<Integer>(k.getMaxIterations())));
		add (new Label("kmeansThreshold", new Model<Double>(k.getClusterClassificationThreshold())));
//        
//        
		
		Form<Void> form = new Form<Void>("form");
        add(form);
		        
     // add a button that can be used to submit the form via ajax
        form.add(new AjaxButton("ajax-button", form)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
            	if (!ps.isRunning()) {
            		try {
            			DataClusteringExecutor.main();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	//target.add(baseUrl);
            	PageParameters pageParameters = new PageParameters();
    			pageParameters.add("selectedTab", 4);
            	setResponsePage(new AdminPage(pageParameters));
            }
            
            @Override
            public boolean isEnabled() {
            	return dataMiningIsAllowed;
            }
            
          
        });
        Label executeNotAllowedLable = new Label("executeNotAllowed", new Model<String>("Please wait until data cleaning process finish."));
		executeNotAllowedLable.setVisible(executeIsAllowed && !dataMiningIsAllowed);
		add (executeNotAllowedLable);
        form.add (executeNotAllowedLable);
	}

}

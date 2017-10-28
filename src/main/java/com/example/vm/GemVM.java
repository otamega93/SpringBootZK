package com.example.vm;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.example.resources.GemResource;
import com.example.services.IGemService;

@VariableResolver(DelegatingVariableResolver.class)
public class GemVM {

	@WireVariable
	private IGemService gemService;
	
	private List<GemResource> gems;
	
	private GemResource selectedGem;
	
	private GemResource newSelectedGem;
	
	// Textbox params
	private String name;
	
	private String description;
	
	private Long ids;
	
	
	/**
	 * Query the DB and fill the list
	 * 
	 * @return
	 * @throws RestClientException
	 * @throws URISyntaxException
	 */
	@Command
	@NotifyChange("gems")
	public List<GemResource> findAll() throws RestClientException, URISyntaxException {
		
		gems = gemService.findAll();
		return gems;
	}
	
	/**
	 * Add new Gem to the gem list and to the DB
	 * 
	 * @return
	 * @throws RestClientException
	 * @throws URISyntaxException
	 */
	@Command
	@NotifyChange("gems")
	public GemResource save(@BindingParam("gemListbox") Listbox listbox) throws RestClientException, URISyntaxException {
		
		GemResource gemResource = new GemResource();
		
		if (this.name == null || description == null) {
			
			Messagebox.show("Neither the field name nor the field description can be empty", "Error", 0, Messagebox.EXCLAMATION);
		
		}
			
		else {	
			
	        EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
	        	
	            public void onEvent(ClickEvent event) throws Exception {
	                if(Messagebox.Button.YES.equals(event.getButton())) {

	                	GemResource SavedGemResource = new GemResource();
	                	
	        			//gemResource.setIds(ids);
	        			gemResource.setName(name);
	        			gemResource.setDescription(description);
	        			SavedGemResource = gemService.save(gemResource);
	        			
	        			// Adding to the list the new resource
	        			gems.add(SavedGemResource);
	        			
	        			// Creating new model for the view with the element deleted
	        			ListModel<GemResource> updatedListModel = new ListModelList<GemResource>(gems);	
	        			
	        			// Pass up the new list to the Model of our listBox
	        			listbox.setModel(updatedListModel);
	        			
	                    Messagebox.show("The Gem has been saved.");
	
	                }
	                
	                else if (Messagebox.Button.NO.equals(event.getButton())) {
	        			
	        			return;
	                }
	            }
	        };
	        Messagebox.show("Are you sure you want to save this Gem?", "Cancel Gem", new Messagebox.Button[]{
	                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
			
			return gemResource;
		}
		
		return gemResource;
	}
	
	/**
	 * Update the data from the list and from the DB
	 * I think this one updates the gems list because when closing the windows it refreshes the main one
	 * 
	 * @param window
	 * @return
	 * @throws RestClientException
	 * @throws URISyntaxException
	 */
	@Command
	@NotifyChange("gems")
	public GemResource update(@BindingParam("cmp") Window window) throws RestClientException, URISyntaxException {
		
		if (newSelectedGem == null || 
				(newSelectedGem.getIds() == null)|| 
				(newSelectedGem.getName() == null || newSelectedGem.getName().trim().isEmpty()) ||
				(newSelectedGem.getDescription() == null || newSelectedGem.getDescription().trim().isEmpty())) {
			
			Messagebox.show("Neither the field name nor the field description can be empty", "Error", 0, Messagebox.EXCLAMATION);
			
			return newSelectedGem;
		}	
		
		// Updating the value of the newSelectedGem for the new one
		newSelectedGem = gemService.update(newSelectedGem);
		
//		// Removing from list so we don't need to actually reload by querying the DB to get the updated data
//		List<GemResource> updatedList = new ArrayList<GemResource>();
//		
//		// Create a new list without the currently deleted object
//		for (GemResource gemResource : gems) {
//			
//			if (!newSelectedGem.getIds().equals(gemResource.getIds())) {
//				updatedList.add(gemResource);
//				//System.out.println(gemResource.getName());
//			} 
//			else if (newSelectedGem.getIds().equals(gemResource.getIds())) {
//				updatedList.add(newSelectedGem);
//				//System.out.println(newSelectedGem);
//			}
//		}
//		
//		// Saving the new list into the previous one so it can trigger the @NotifyChange("gems")
//		gems = updatedList;
		
		// Closing the window
		window.detach();
		
		return newSelectedGem;
		
	}
	
	/**
	 * Delete the selected item from list and DB
	 * 
	 * @param listbox
	 * @return
	 * @throws RestClientException
	 * @throws URISyntaxException
	 */
	// The @BindingParam listbox below is not doing anything but I'll leave it there so I can remember that one could do that too
	@Command
	@NotifyChange("gems")
	public ResponseEntity<?> delete(@BindingParam("gemListbox") Listbox listbox) throws RestClientException, URISyntaxException {
		
		if (newSelectedGem != null && newSelectedGem.getIds() != null) {
			
			// Deleting the actual thing from DB
			ResponseEntity<?> responseEntity = gemService.delete(newSelectedGem);
			
			// Removing from list so we don't need to actually reload by querying the DB to get the updated data
			List<GemResource> updatedList = new ArrayList<GemResource>();
			
			// Create a new list without the currently deleted object
			for (GemResource gemResource : gems) {
				
				if (!newSelectedGem.getIds().equals(gemResource.getIds())) {
					updatedList.add(gemResource);
					//System.out.println(gemResource.getName());
				}
			}
			
			// Saving the new list into the previous one so it can trigger the @NotifyChange("gems")
			gems = updatedList;
			
			/**
			 * Below is no longer necessary since gems is now being actually updated
			 */
			
//			// Creating new model for the view with the element deleted
//			ListModel<GemResource> updatedListModel = new ListModelList<GemResource>(gems);	
//			
//			// Pass up the new list to the Model of our listBox
//			listbox.setModel(updatedListModel);
			
			// Clearing the selects so it doesn't show errors when clicking again after a delete 
			selectedGem = null;
			newSelectedGem = null;
			
			return responseEntity;
		}
		
		else
			Messagebox.show("You must select one gem from the list above", "Error", 0, Messagebox.ERROR);
			return null;
		
	}
	
	/**
	 * Command for cleaning up the textboxes in the zul file
	 * 
	 */
	@Command
	public void clear(@BindingParam("nameLabel") Textbox nameLabel, @BindingParam("descriptionLabel") Textbox descriptionLabel) {
		
		// Clean the actual variables which feeds the fields
		this.setIds(null);;
		this.setName(null);
		this.setDescription(null);
		
		// Clean the visual Zul Textboxes
		nameLabel.setValue(null);
		descriptionLabel.setValue(null);
		
	}
	
	
	/**
	 * This obtains the object newSelectedGem and passes to the View to be opened (gemEdit.zul in this case)
	 * @param view
	 * @param execution
	 */
	@Init
	public void init (@ContextParam(ContextType.VIEW) Component view, 
			@ContextParam(ContextType.EXECUTION) Execution execution) {
		newSelectedGem = (GemResource) execution.getArg().get("newSelectedGem");
	}
	
	/**
	 * Command to open a new window over the current one (the gemEdit.zul). 
	 * 
	 */
	@Command
	@NotifyChange({"selectedGem", "gems"})
	public void edit() {
		if (selectedGem != null) {
			Map parameters = new HashMap();
			parameters.put("newSelectedGem", selectedGem);
			Component zulComponent = Executions.createComponents("gemEdit.zul", null, parameters);
			if (zulComponent instanceof Window) {
				Window window = (Window) zulComponent;
				/**
				 *  Show zul as modal (mini window popup dialog like). There five modes availables for windows:  
				 *  Embedded (default), modal (the one for this gemEdit.zul), overlapped, popup, highlighted 
				 * 
				 */
				window.doModal();
			}
		}
	}
	
	

	/**
	 * Getters and Setters
	 * 
	 */
	
	public List<GemResource> getGems() {
		return gems;
	}

	public void setGems(List<GemResource> gems) {
		this.gems = gems;
	}

	public GemResource getSelectedGem() {
		return selectedGem;
	}

	public void setSelectedGem(GemResource selectedGem) {
		this.selectedGem = selectedGem;
	}

	public GemResource getNewSelectedGem() {
		return newSelectedGem;
	}

	public void setNewSelectedGem(GemResource newSelectedGem) {
		this.newSelectedGem = newSelectedGem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getIds() {
		return ids;
	}

	public void setIds(Long ids) {
		this.ids = ids;
	}
	
}
/**
 * 
 */
package test.com.anthem.nimbus.platform.spec.model.comamnd;

import java.util.LinkedList;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;

/**
 * @author Soham Chakravarti
 *
 */
public class TestCommandFactory {
	
	public static Command create() {
		Command c = new Command("/anthem/comm/hrs/p/member/_search");
		
		c.createRoot(Type.ClientAlias, "anthem")
			.createNext(Type.ClientOrgAlias, "comm")
			.createNext(Type.AppAlias, "hrs")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "member")
			;//.createNext(Type.Action, Action._search.name());
		c.setAction(Action._search);
		return c;
	}
	
	
	public static Command create_view_searchPatient() {

		Command c = new Command("/anthem/comm/icr/p/flow_searchpatient/_new");

		c.createRoot(Type.ClientAlias, "anthem")
			.createNext(Type.ClientOrgAlias, "comm")
			.createNext(Type.AppAlias, "icr")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_searchpatient");
		c.setAction(Action._new);
		return c;
	}
	
	public static Command create_view_icr_UMCaseFlow() {

		Command c = new Command("/anthem/comm/icr/p/view_umcase/_new");

		c.createRoot(Type.ClientAlias, "anthem")
			.createNext(Type.ClientOrgAlias, "comm")
			.createNext(Type.AppAlias, "icr")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "view_umcase")
			;//.createNext(Type.Action, Action._new.name());
		c.setAction(Action._new);
		return c;
	}
	
	
	
	public static Command create_view_UserRoleFlow() {

		Command c = new Command("/anthem/comm/icr/p/flow_user-role/_new");

		c.createRoot(Type.ClientAlias, "anthem")
			.createNext(Type.ClientOrgAlias, "comm")
			.createNext(Type.AppAlias, "icr")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_user-role")
			.createNext(Type.ProcessAlias, "_submitUserRole");
		c.setAction(Action._new);
		return c;
	}
	

	public static Command create_icr_UMCaseFlow_findPatient() {

		Command c = new Command("/anthem/comm/icr/p/flow_umcase/_findPatient");

		c.createRoot(Type.ClientAlias, "anthem")
			.createNext(Type.ClientOrgAlias, "comm")
			.createNext(Type.AppAlias, "icr")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_umcase")
			.createNext(Type.DomainAlias, "_findPatient");
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute);
		
		return c;
	}

	public static Command icr_requestType_Update() {
		Command c = new Command("/anthem/comm/icr/p/flow_umcase/pg1/caseInfo/requestType/_update");
		
		c.createRoot(Type.ClientAlias, "anthem")
			.createNext(Type.ClientOrgAlias, "comm")
			.createNext(Type.AppAlias, "icr")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_umcase")
			.createNext(Type.DomainAlias, "pg1")
			.createNext(Type.DomainAlias, "caseInfo")
			.createNext(Type.DomainAlias, "requestType")
			;
		c.setAction(Action._update);
		c.templateBehaviors().add(Behavior.$execute);
		
		return c;
	}
	
	public static Command autocase_Update() {
		Command c = new Command("/anthem/comm/icr/p/flow_autocase/pg3/caseName/_update");
		
		c.createRoot(Type.ClientAlias, "anthem")
			.createNext(Type.ClientOrgAlias, "comm")
			.createNext(Type.AppAlias, "icr")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_autocase")
			.createNext(Type.DomainAlias, "pg3")
			.createNext(Type.DomainAlias, "caseName")
			;
		c.setAction(Action._update);
		c.templateBehaviors().add(Behavior.$execute);
		
		return c;
	}

	public static Command cmd_userRoleManagementAlias() {
		Command c = new Command("/abc/admin/p/flow_userrole/_new");
	
		c.createRoot(Type.ClientAlias, "abc")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_userrole")
		;
		c.setAction(Action._new);
		return c;
	}
	
	public static Command cmd_autoCaseNew() {
		Command c = new Command("/abc/admin/p/flow_autocase/_new");
	
		c.createRoot(Type.ClientAlias, "abc")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_autocase")
		;
		c.setAction(Action._new);
		c.templateBehaviors().add(Behavior.$config);
		return c;
	}
	
	public static Command cmd_autoCaseNavNext() {
		Command c = new Command("/abc/admin/p/flow_autocase/_dummyProcess/_process");
	
		c.createRoot(Type.ClientAlias, "abc")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_autocase")
			.createNext(Type.ProcessAlias, "_dummyProcess")
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute);
		return c;
	}
	
	
	public static Command cmd_autoCaseBreadCrumbNav() {
		Command c = new Command("/xyz/admin/p/flow_autocase/_nav");
	
		c.createRoot(Type.ClientAlias, "abc")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_autocase")
			.createNext(Type.ProcessAlias, "_nav")
		;
		
		c.setAction(Action._nav);
		c.templateBehaviors().add(Behavior.$nav);
		return c;
	}
	
	public static Command cmd_userManagementAlias() {
		Command c = new Command("/platform/admin/p/flow_clientuser/_new");
	
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_clientuser")
		;
		c.setAction(Action._new);
		return c;
	}
	
	public static Command cmd_submitUserManagementAlias() {
		Command c = new Command("/platform/admin/p/flow_clientuser/_submitClientUser/_process");
	
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_clientuser")
			.createNext(Type.ProcessAlias, "_submitClientUser")
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute).add(Behavior.$nav);
		return c;
	}
	
	public static Command cmd_clientManagementAlias() {
		Command c = new Command("/platform/admin/p/flow_client/_new");
	
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_client")
		;
		c.setAction(Action._new);
		return c;
	}
	
	public static Command cmd_createCaseAlias() {
		Command c = new Command("/platform/admin/p/flow_cmcase/_createCase:586c1d3ee33f2c95c5fc1e01/_process");
		//Command(absoluteUri=/platform/admin/p/flow_cmcase/_createCase:58668264f8d088172657b9f5/_process, clientUserId=null, event=null, currentBehaviorIndex=-1, behaviors=[$execute, $config])
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase")
			.createNext(Type.ProcessAlias, "_createCase:586c1d3ee33f2c95c5fc1e01")
			
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute).add(Behavior.$config);
		
		return c;
	}
	
	public static Command cmd_createInterventionAlias() {
		Command c = new Command("/platform/admin/p/intervention/_create");
	//"/addIntervention/section_addIntervention/form_addIntervention"
		//"/platform/admin/p/intervention/_delete"
		///platform/admin/p/intervention/_create"
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "intervention")
			.createNext(Type.ProcessAlias, "_create")
			
		;
		c.setAction(Action._new);
		c.templateBehaviors().add(Behavior.$execute).add(Behavior.$config);
		
		return c;
	}

    public static Command cmd_createProgramAlias() {
        Command c = new Command("/platform/admin/p/flow_cmprogram/_create/_process");

        c.createRoot(Type.ClientAlias, "platform")
                .createNext(Type.AppAlias, "admin")
                .createNext(Type.PlatformMarker, "p")
                .createNext(Type.DomainAlias, "flow_cmprogram")
                .createNext(Type.ProcessAlias, "_create")

        ;
        c.setAction(Action._new);
        c.templateBehaviors().add(Behavior.$execute);

        return c;
    }

    public static Command cmd_getAllprograms() {
        Command c = new Command("/platform/admin/p/flow_cmprogram/_search/_process");

        c.createRoot(Type.ClientAlias, "platform")
                .createNext(Type.AppAlias, "admin")
                .createNext(Type.PlatformMarker, "p")
                .createNext(Type.DomainAlias, "flow_cmprogram")
                .createNext(Type.ProcessAlias, "_search")

        ;
        c.setAction(Action._search);
        c.templateBehaviors().add(Behavior.$execute);

        return c;
    }
    
    public static Command cmd_searchAutocase() {
        Command c = new Command("/platform/admin/p/autocase/_search/_process");

        c.createRoot(Type.ClientAlias, "platform")
                .createNext(Type.AppAlias, "admin")
                .createNext(Type.PlatformMarker, "p")
                .createNext(Type.DomainAlias, "autocase")
                .createNext(Type.ProcessAlias, "_search")

        ;
        c.setAction(Action._search);
        c.templateBehaviors().add(Behavior.$execute);

        return c;
    }


    public static Command cmd_getCaseAlias() {
		Command c = new Command("/platform/admin/p/flow_cmcase:57fd4ee7f5a6367af90d53b5/_get");
	
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase:57fd4ee7f5a6367af90d53b5")
			
		;
		c.setAction(Action._get);
		
		LinkedList<Behavior> behaviors = new LinkedList<>();
		behaviors.add(Behavior.$execute);
		behaviors.add(Behavior.$config);
		c.setBehaviors(behaviors);
		return c;
	}
    
    public static Command cmd_getCaseAliasForIntervention() {
		Command c = new Command("/platform/admin/p/flow_cmcase/_displayGoals/_process");
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase")
			.createNext(Type.ProcessAlias, "_displayGoals")
			
		;
		c.setAction(Action._process);
		
		LinkedList<Behavior> behaviors = new LinkedList<>();
		behaviors.add(Behavior.$execute);
		behaviors.add(Behavior.$nav);
		c.setBehaviors(behaviors);
		return c;
	}
    
    public static Command cmd_getcaseAliasForBarrier(){
    	Command c = new Command("/platform/admin/p/flow_cmcase/_displayGoals/_process");
    	c.createRoot(Type.ClientAlias, "platform")
    	 .createNext(Type.AppAlias, "admin")
    	 .createNext(Type.PlatformMarker, "p")
    	 .createNext(Type.DomainAlias, "flow_cmcase")
    	 .createNext(Type.ProcessAlias, "_displayGoals");
    	
    	c.setAction(Action._process);
    	LinkedList<Behavior> behavior = new LinkedList<>();
    	behavior.add(Behavior.$execute);
    	behavior.add(Behavior.$nav);
    	c.setBehaviors(behavior);
    	return c;
    }
    
    public static Command cmd_createHealthProblems(){
    	Command c = new Command("/platform/admin/p/flow_cmcase/_submitHealthProblem/_process");
    	c.createRoot(Type.ClientAlias, "platform")
    	 .createNext(Type.AppAlias, "admin")
    	 .createNext(Type.PlatformMarker, "p")
    	 .createNext(Type.DomainAlias, "flow_cmcase")
    	 .createNext(Type.ProcessAlias, "_submitHealthProblem");
    	
    	c.setAction(Action._process);
    	LinkedList<Behavior> behavior = new LinkedList<>();
    	behavior.add(Behavior.$execute);
    	behavior.add(Behavior.$nav);
    	c.setBehaviors(behavior);
    	return c;
    }
	
	public static Command cmd_patient_search() {
		Command c = new Command("/xyz_client/abc_app/p/patient/_search");
	
		c.createRoot(Type.ClientAlias, "xyz_client")
			.createNext(Type.AppAlias, "abc_app")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "patient")	
		;
		c.setAction(Action._search);
		c.templateBehaviors().add(Behavior.$execute);
		
		return c;
	}
	
	public static Command cmd_enrollCaseAlias() {
		Command c = new Command("/abc/admin/p/flow_cmcase:57fef31fb42eb97ef549fe64/_enroll/_process?b=$executeAnd$save");
	
		c.createRoot(Type.ClientAlias, "abc")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase:57fef31fb42eb97ef549fe64")
			.createNext(Type.ProcessAlias, "_enroll")
			
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute).add(Behavior.$execute).add(Behavior.$save);
		
		return c;
	}
	
	public static Command cmd_editCaseAlias() {
		Command c = new Command("/abc/admin/p/flow_cmcase:580e634ab9a03d74ba11babd/_editCase/_process?b=$executeAnd$config");
	
		c.createRoot(Type.ClientAlias, "abc")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase:580e634ab9a03d74ba11babd")
			.createNext(Type.ProcessAlias, "_editCase")
			
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute).add(Behavior.$config);
		
		return c;
	}
	
	public static Command cmd_createCaseWithExecuteAlias() {
		Command c = new Command("/platform/admin/p/flow_cmcase/_new");
	
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase")
			
		;
		c.setAction(Action._new);
		c.templateBehaviors().add(Behavior.$execute);
		
		return c;
	}
	
	public static Command cmd_patient_get(String refId) {
		Command c = new Command("/platform/admin/p/patient:"+refId+"/_get");
	
		c.createRoot(Type.ClientAlias, "xyz_client")
			.createNext(Type.AppAlias, "abc_app")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "patient:"+refId)	
		;
		c.setAction(Action._get);
		c.templateBehaviors().add(Behavior.$execute);
		
		return c;
	}
	
	public static Command cmd_cmcase_search() {
		Command c = new Command("/platform/admin/p/cmcase/_search");
		c.createRoot(Type.ClientAlias, "platform")
		.createNext(Type.AppAlias, "admin")
		.createNext(Type.PlatformMarker, "p")
		.createNext(Type.DomainAlias, "cmcase")
		.createNext(Type.ProcessAlias, "_search" )
		;
		c.setAction(Action._search);
		c.templateBehaviors().add(Behavior.$execute);
		return c;
		}
	
	public static Command cmd_editBarriers(String refId) {
		Command c = new Command("/platform/admin/p/flow_cmcase/_displayBarriers:"+refId+"/_process");
	
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase")
			.createNext(Type.ProcessAlias, "_displayBarriers:"+refId)
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute);
		c.templateBehaviors().add(Behavior.$nav);
		
		return c;
	}
	
	public static Command cmd_patient_new() {
		Command c = new Command("/abc/admin/p/flow_search-patient/_new");
	
		c.createRoot(Type.ClientAlias, "abc")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_search-patient")
		;
		c.setAction(Action._new);
		return c;
	}

	public static Command cmd_program_new() {
		Command c = new Command("/abc/admin/p/flow_cmcase/cmcase/cmProgram/cmProgramEntry/_new");
	
		c.createRoot(Type.ClientAlias, "abc")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase")
			.createNext(Type.DomainAlias, "cmcase")
			.createNext(Type.DomainAlias, "cmProgram")
			.createNext(Type.DomainAlias, "cmProgramEntry")
		;
		c.setAction(Action._new);
		c.templateBehaviors().add(Behavior.$execute);

		return c;
	}
	
	public static Command cmd_patient_delete(String refId) {
		Command c = new Command("/platform/admin/p/patient:"+refId+"/_delete");
	
		c.createRoot(Type.ClientAlias, "xyz_client")
			.createNext(Type.AppAlias, "abc_app")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "patient:"+refId)	
		;
		c.setAction(Action._delete);
		c.templateBehaviors().add(Behavior.$execute);
		
		return c;
	}
	
	public static Command cmd_createCarePlanAlias() {
		Command c = new Command("/platform/admin/p/flow_careplan1/_submitCarePlan/_process");
	
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_careplan1")
			.createNext(Type.ProcessAlias, "_submitCarePlan")
			
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute);
		
		return c;
	}
	
	public static Command cmd_createGoalsAlias() {
		Command c = new Command("/platform/admin/p/flow_cmcase/_submitGoal/_process");
	
		c.createRoot(Type.ClientAlias, "platform")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase")
			.createNext(Type.ProcessAlias, "_submitGoal")
			
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute);
		c.templateBehaviors().add(Behavior.$nav);
		
		return c;
	}
	
	public static Command cmd_cmcase_new() {
		Command c = new Command("/abc/admin/p/flow_cmcase/_new");
	
		c.createRoot(Type.ClientAlias, "abc")
			.createNext(Type.AppAlias, "admin")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase")
		;
		c.setAction(Action._new);
		return c;
	}
	
	public static Command cmd_cmcase_get(String refId) {
		Command c = new Command("/platform/admin/p/cmcase:"+refId+"/_get");
	
		c.createRoot(Type.ClientAlias, "xyz_client")
			.createNext(Type.AppAlias, "abc_app")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "cmcase:"+refId)	
		;
		c.setAction(Action._get);
		c.templateBehaviors().add(Behavior.$execute);
		
		return c;
	}
	
	
	public static Command cmd_create_barrier(String refId) {
		Command c = new Command("/flow_cmcase/_addBarrier:{"+refId+"}/_process");
	
		c.createRoot(Type.ClientAlias, "xyz_client")
			.createNext(Type.AppAlias, "abc_app")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase")	
			.createNext(Type.ProcessAlias, "_addBarrier:{"+refId+"}")
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute);
		c.templateBehaviors().add(Behavior.$nav);
		
		return c;
	}
	
	public static Command cmd_editGoal(String refId) {
		Command c = new Command("/flow_cmcase/_editGoal:{"+refId+"}/_process");
	
		c.createRoot(Type.ClientAlias, "xyz_client")
			.createNext(Type.AppAlias, "abc_app")
			.createNext(Type.PlatformMarker, "p")
			.createNext(Type.DomainAlias, "flow_cmcase")	
			.createNext(Type.ProcessAlias, "_editGoal:{"+refId+"}")
		;
		c.setAction(Action._process);
		c.templateBehaviors().add(Behavior.$execute);
		c.templateBehaviors().add(Behavior.$nav);
		return c;
	}
	
}

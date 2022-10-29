package dto.assignment;

import java.util.ArrayList;
import java.util.List;

public class AssignmentDTOList {

    private List<AssignmentDTO> assignments = null;

    private boolean hasMore = true;

    public AssignmentDTOList (List<AssignmentDTO> assignments, boolean hasMore){
        this.assignments = assignments;
        this.hasMore = hasMore;
    }

    public AssignmentDTOList() {
        this.assignments = new ArrayList<>();
        this.hasMore = false;
    }

    public List<AssignmentDTO> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignmentDTO> assignments) {
        this.assignments = assignments;
    }

    public boolean hasMore() {
        return hasMore;
    }
}

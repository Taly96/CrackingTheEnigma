package dto.assignment;

import java.util.List;

public class AssignmentDTOList {

    private List<AssignmentDTO> assignments = null;

    private boolean hasMore = true;

    public AssignmentDTOList (List<AssignmentDTO> assignments, boolean hasMore){
        this.assignments = assignments;
        this.hasMore = hasMore;
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

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}

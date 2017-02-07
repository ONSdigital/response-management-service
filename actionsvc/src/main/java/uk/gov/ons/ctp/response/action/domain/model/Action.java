package uk.gov.ons.ctp.response.action.domain.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;

/**
 * Domain model object.
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "action", schema = "action")
public class Action implements Serializable {

  private static final long serialVersionUID = 8539984354009320104L;

  /**
   * Priority of action
   * NOTE: the names need to match those in the outbound xsd
   */
  public enum ActionPriority {
    HIGHEST(1, "highest"),
    HIGHER(2, "higher"),
    MEDIUM(3, "medium"),
    LOWER(4, "lower"),
    LOWEST(5, "lowest");

    private final int level;   // numeric level
    private final String name; // the level name

    private static Map<Integer, ActionPriority> map = new HashMap<Integer, ActionPriority>();

    static {
        for (ActionPriority priority : ActionPriority.values()) {
            map.put(priority.level, priority);
        }
    }

    /**
     * return the enum for an integer level arg
     * @param priorityLevel the int value
     * @return the enum
     */
    public static ActionPriority valueOf(int priorityLevel) {
        return map.get(priorityLevel);
    }

    /**
     * Create an instance of the enum
     * @param value priority as integere
     * @param label verbage
     */
    ActionPriority(int value, String label) {
      this.level = value;
      this.name = label;
    }

    /**
     * Getter
     * @return gotten
     */
    public int getLevel() {
      return this.level;
    }

    /**
     * Getter
     * @return gotten
     */
    public String getName() {
      return this.name;
    }
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "actionid")
  private BigInteger actionId;

  @Column(name = "caseid")
  private Integer caseId;

  @Column(name = "actionplanid")
  private Integer actionPlanId;

  @Column(name = "actionruleid")
  private Integer actionRuleId;

  @Column(name = "createdby")
  private String createdBy;

  @Column(name = "manuallycreated")
  private Boolean manuallyCreated;

  @ManyToOne
  @JoinColumn(name = "actiontypeid")
  private ActionType actionType;

  private Integer priority;

  private String situation;

  @Enumerated(EnumType.STRING)
  private ActionDTO.ActionState state;

  @Column(name = "createddatetime")
  private Timestamp createdDateTime;

  @Column(name = "updateddatetime")
  private Timestamp updatedDateTime;

  @Version
  @Column(name = "optlockversion")
  private int optLockVersion;
}

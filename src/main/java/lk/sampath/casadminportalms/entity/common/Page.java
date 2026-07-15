package lk.sampath.casadminportalms.entity.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page<T extends Serializable> implements Serializable {

  private static final long serialVersionUID = 3617869045664714881L;

  private long totalRecords = 0;

  private int startIndex = 0;

  private int noOfRecords = 0;

  private int pageLength = 0;

  private Collection<T> page = new ArrayList<T>();

  public Page() {}

  public Page(long totalRecords, int startIndex, int noOfRecords, Collection<T> page) {
    this.totalRecords = totalRecords;
    this.startIndex = startIndex;
    this.noOfRecords = noOfRecords;
    this.page = page;
  }

  public Page(
      long totalRecords, int startIndex, int noOfRecords, int pageLength, Collection<T> page) {
    this.totalRecords = totalRecords;
    this.startIndex = startIndex;
    this.noOfRecords = noOfRecords;
    this.pageLength = pageLength;
    this.page = page;
  }

  public int getTotalNoOfPages() {
    int totalNoOfPages = 1;
    if (pageLength != 0) {
      totalNoOfPages = (int) totalRecords / pageLength;
      if (totalRecords % pageLength > 0) totalNoOfPages++;
    }
    return totalNoOfPages;
  }

  public int getCurrentPageNo() {
    int currentPageNo = 1;
    if (pageLength != 0) {
      currentPageNo = startIndex / pageLength + 1;
    }
    return currentPageNo;
  }

  public List<T> toList() {
    return new ArrayList<>(page);
  }
}

package domain.services.serviceHogares;


import java.util.List;

public class ListadoHogares {

  public int total;
  public int offset;
  public List<HogarTransitorio> hogares;

  public List<HogarTransitorio> getHogares() {
    return hogares;
  }

  public ListadoHogares(int total, int offset, List<HogarTransitorio> hogares) {
    this.total = total;
    this.offset = offset;
    this.hogares = hogares;
  }
}

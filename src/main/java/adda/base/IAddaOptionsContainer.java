package adda.base;

import java.util.List;

public interface IAddaOptionsContainer {
    String getLabel();
    String getDescription();

    List<IAddaOption> getAddaOptions();
}

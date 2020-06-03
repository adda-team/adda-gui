package adda.item.root.projectTree;

import adda.base.annotation.*;
import adda.base.boxes.BoxBaseObservable;
import adda.base.models.IModel;
import adda.base.views.IView;

@BindModel
@BindView
@BindController
public class ProjectTreeBox extends BoxBaseObservable {

    protected void initView(IModel model) {
        IView view = getAssociatedView();
        //model.addObserver(view);
        view.initFromModel(model);
        this.view = view;
    }
}

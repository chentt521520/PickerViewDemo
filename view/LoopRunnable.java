package com.winner.pickerview.view;

final class LoopRunnable implements Runnable {

    private final LoopView loopView;

    LoopRunnable(LoopView loopview) {
        super();
        loopView = loopview;

    }

    @Override
    public final void run() {
        LoopListener listener = loopView.loopListener;
        int selectedItem = LoopView.getSelectedItem(loopView);
        loopView.arrayList.get(selectedItem);
        listener.onItemSelect(selectedItem);
    }
}

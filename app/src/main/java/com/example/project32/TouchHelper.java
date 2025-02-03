package com.example.project32;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project32.Adapter.ToDoAdapter;

public class TouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter;
    private Drawable deleteIcon, editIcon;
    private final ColorDrawable deleteBackground = new ColorDrawable(Color.RED);

    private Paint clearPaint;

    public TouchHelper(ToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.deleteIcon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.delete);
        clearPaint = new Paint();
        clearPaint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {
            // Delete confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setMessage("Are you sure?")
                    .setTitle("Delete Task")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteTask(position);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(position);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            adapter.notifyItemChanged(position);
        }
    }


        @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            int itemViewTop = viewHolder.itemView.getTop();
            int itemViewBottom = viewHolder.itemView.getBottom();
            int itemViewLeft = viewHolder.itemView.getLeft();
            int itemViewRight = viewHolder.itemView.getRight();
            int itemHeight = itemViewBottom - itemViewTop;

            if (dX > 0) {
                deleteBackground.setBounds(itemViewLeft, itemViewTop, itemViewLeft + (int) dX, itemViewBottom);
                deleteBackground.draw(c);

                if (deleteIcon != null) {
                    int iconMargin = (itemHeight - deleteIcon.getIntrinsicHeight()) / 2;
                    int iconLeft = itemViewLeft + iconMargin;
                    int iconRight = iconLeft + deleteIcon.getIntrinsicWidth();
                    int iconTop = itemViewTop + (itemHeight - deleteIcon.getIntrinsicHeight()) / 2;
                    int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    deleteIcon.draw(c);
                }
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}

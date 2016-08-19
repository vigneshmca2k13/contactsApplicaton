package first.project.com.firstproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Animation_Activity extends AppCompatActivity {
Button animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_);

        animation = (Button)findViewById(R.id.animate);
        animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AnimatorSet set = new AnimatorSet();
//                set.playSequentially(
//                        ObjectAnimator.ofFloat(animation, "scaleX", 1.0f, 2.0f)
//                                .setDuration(2000),
//                        ObjectAnimator.ofFloat(animation, "scaleX", 2.0f, 1.0f)
//                                .setDuration(2000)
//                );
//                set.start();
                animation.animate().alpha(0.2f).xBy(-100).yBy(100);
            }

    });


    }
}

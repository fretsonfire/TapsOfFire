package app.tapsoffire.ui.components;

import java.util.Random;
import app.tapsoffire.R;
import app.tapsoffire.ui.helpers.UISoundEffects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import javax.inject.Inject;

public class FireButton extends TextButton implements View.OnClickListener
{
    @Inject protected UISoundEffects uiSoundEffects;

    private View.OnClickListener m_clickListener;
    private Animation m_animation;
    private boolean m_clicking;

    private BitmapShader m_fireShader;
    private Matrix m_fireShaderMatrix;

    public FireButton(Context context) {
        super(context);
        setup();
    }

    public FireButton(Context context,AttributeSet attrs) {
        super(context,attrs);
        setup();
    }

    public FireButton(Context context,AttributeSet attrs,int defStyle) {
        super(context,attrs,defStyle);
        setup();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.m_clickListener = listener;
    }

    public void setAnimation(Animation animation) {
        endClick();
        super.setAnimation(animation);
    }

    public void startAnimation(Animation animation) {
        endClick();
        super.startAnimation(animation);
    }

    public void clearAnimation() {
        endClick();
        super.clearAnimation();
    }

    public void onClick(View view) {
        uiSoundEffects.playInSound();
        Animation currentAnimation = getAnimation();
        if (!m_clicking && currentAnimation != null && !currentAnimation.hasEnded()) {
            click();
        } else {
            startClick();
        }
    }

    public void onAnimationEnd() {
        super.onAnimationEnd();
        endClick();
    }

    private void startClick() {
        super.startAnimation(m_animation);
    }

    private void endClick() {
        if (m_clicking) {
            click();
            super.setAnimation(null);
        }
    }

    private void click() {
        m_clicking = false;
        if (m_clickListener != null) {
            m_clickListener.onClick(this);
        }
    }

    private void setup() {
        super.setOnClickListener(this);
        m_animation = AnimationUtils.loadAnimation(getContext(), R.anim.squash);
    }
}

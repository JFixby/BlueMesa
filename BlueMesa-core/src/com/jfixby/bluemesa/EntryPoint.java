
package com.jfixby.bluemesa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jfixby.bluemesa.sqs.MessagesConsumer;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.log.MESSAGE_MARKER;
import com.jfixby.scarabei.api.util.JUtils;

public class EntryPoint extends ApplicationAdapter implements MessagesConsumer {
	SpriteBatch batch;
	private BitmapFont font;
	// Texture img;
	private List<LogMessage> stack;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.font = new BitmapFont();
		this.font.setColor(Color.WHITE);
		final ScreenLogger logger = new ScreenLogger(this);
		this.stack = Collections.newList();
		L.deInstallCurrentComponent();
		L.installComponent(logger);

// L.in
// this.img = new Texture("badlogic.jpg");

	}

	@Override
	public synchronized void render () {
		Gdx.gl.glClearColor(0.01f, 0.3f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.batch.begin();

		final float H = this.font.getLineHeight();
		if (this.stack != null) {
			for (int i = 0; i < this.stack.size(); i++) {
				final int k = this.stack.size() - 1 - i;
				final LogMessage m = this.stack.getElementAt(k);
				this.font.setColor(this.colorOf(m.mode));
				this.font.draw(this.batch, m.string + "", 10, 50 + i * H);
			}
		}
// this.batch.draw(this.img, 0, 0);
		this.batch.end();
	}

	private Color colorOf (final MESSAGE_MARKER mode) {
		if (mode == MESSAGE_MARKER.NORMAL) {
			return Color.WHITE;
		}
		return Color.RED;
	}

	@Override
	public void dispose () {
		this.batch.dispose();
		this.font.dispose();
// this.img.dispose();
	}

	@Override
	public synchronized void append (final String messageText, final MESSAGE_MARKER mode) {

		final List<String> append = JUtils.split(messageText, "\n");
		for (final String value : append) {
			final LogMessage m = new LogMessage();
			m.mode = mode;
			m.string = value;
			this.stack.add(m);
		}

		while (this.stack.size() > 500) {
			this.stack.removeElementAt(0);
		}
		;
// this.stack.reverse();
	}
}

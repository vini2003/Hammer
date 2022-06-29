package dev.vini2003.hammer.component.api.common.component;

public interface PlayerComponent extends Component {
	default boolean shouldCopyOnDeath() {
		return false;
	}
}

package com.northconcepts.datapipeline.internal.xpath;

public enum Axis {
	ANCESTOR(PrincipalNodeType.ELEMENT, Direction.REVERSE, true, false), ANCESTOR_OR_SELF(PrincipalNodeType.ELEMENT, Direction.REVERSE, true, false), ATTRIBUTE(
			PrincipalNodeType.ATTRIBUTE, (Direction) null, false, true) {
		@Override
		public boolean test(final Step step, final XmlNode node) {
			if (!step.isfirst()) {
				return step.matchesElement(node);
			}
			if (node.hasParent()) {
				final Axis axis = step.getLocationPath().getType().getAxis();
				if (!axis.isSupported()) {
					throw new UnsupportedOperationException(axis + " axis is unsupported");
				}
				if (axis.getDirection() != Direction.FORWARD || !axis.isRecursive()) {
					return false;
				}
			}
			return step.matchesElement(node);
		}
	},
	CHILD(PrincipalNodeType.ELEMENT, Direction.FORWARD, false, true) {
		@Override
		public boolean test(final Step step, final XmlNode node) {
			if (!node.hasParent()) {
				return false;
			}
			if (!step.matchesElement(node)) {
				return false;
			}
			if (step.hasPreviousStep()) {
				return step.getPreviousStep().matches(node.getParent());
			}
			return !node.hasGrandParent();
		}
	},
	DESCENDANT(PrincipalNodeType.ELEMENT, Direction.FORWARD, true, true) {
		@Override
		public boolean test(Step step, XmlNode node) {
			if (!node.hasParent()) {
				return false;
			}
			if (!step.matchesElement(node)) {
				return false;
			}
			if (step.hasPreviousStep()) {
				step = step.getPreviousStep();
				do {
					node = node.getParent();
					if (step.matches(node)) {
						return true;
					}
				} while (node.hasParent());
				return false;
			}
			return true;
		}
	},
	DESCENDANT_OR_SELF(PrincipalNodeType.ELEMENT, Direction.FORWARD, true, true) {
		@Override
		public boolean test(final Step step, final XmlNode node) {
			return Axis.SELF.test(step, node) || Axis.DESCENDANT.test(step, node);
		}
	},
	FOLLOWING(PrincipalNodeType.ELEMENT, Direction.FORWARD, true, false), FOLLOWING_SIBLING(PrincipalNodeType.ELEMENT, Direction.FORWARD, true, false), NAMESPACE(
			PrincipalNodeType.NAMESPACE, (Direction) null, false, true) {
		@Override
		public boolean test(final Step step, final XmlNode node) {
			return false;
		}
	},
	PARENT(PrincipalNodeType.ELEMENT, Direction.REVERSE, false, false), PRECEDING(PrincipalNodeType.ELEMENT, Direction.REVERSE, true, false), PRECEDING_SIBLING(
			PrincipalNodeType.ELEMENT, Direction.REVERSE, true, false), SELF(PrincipalNodeType.ELEMENT, (Direction) null, false, true) {
		@Override
		public boolean test(final Step step, final XmlNode node) {
			if (!step.matchesElement(node)) {
				return false;
			}
			if (step.hasPreviousStep()) {
				return step.getPreviousStep().matches(node.getParent());
			}
			return !node.hasParent();
		}
	};

	private final PrincipalNodeType principalNodeType;
	private final Direction direction;
	private final boolean recursive;
	private final boolean supported;

	public static Axis lookup(String name) {
		if (name == null) {
			return null;
		}
		name = name.toUpperCase();
		name = name.replaceAll("-", "_");
		return valueOf(name);
	}

	private Axis(final PrincipalNodeType principalNodeType, final Direction direction, final boolean recursive, final boolean supported) {
		this.principalNodeType = principalNodeType;
		this.direction = direction;
		this.recursive = recursive;
		this.supported = supported;
	}

	public PrincipalNodeType getPrincipalNodeType() {
		return this.principalNodeType;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public boolean isRecursive() {
		return this.recursive;
	}

	public boolean isSupported() {
		return this.supported;
	}

	public boolean test(final Step step, final XmlNode node) {
		throw new UnsupportedOperationException(this + " axis is unsupported");
	}
}

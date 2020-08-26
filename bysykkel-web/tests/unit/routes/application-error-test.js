import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | application-error', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:application-error');
    assert.ok(route);
  });
});

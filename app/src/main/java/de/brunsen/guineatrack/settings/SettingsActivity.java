package de.brunsen.guineatrack.settings;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import de.brunsen.guineatrack.R;
import de.brunsen.guineatrack.ui.activities.BaseActivity;

public class SettingsActivity extends BaseActivity implements SettingsView{

    @BindView(R.id.settings_weight_switch)
    Switch weightSwitch;
    @BindView(R.id.settings_origin_switch)
    Switch originSwitch;
    @BindView(R.id.settings_limitations_switch)
    Switch limitationsSwitch;
    @BindView(R.id.settings_entry_switch)
    Switch entrySwitch;
    @BindView(R.id.settings_departure_switch)
    Switch departureSwitch;
    @BindView(R.id.settings_castration_switch)
    Switch castrationSwitch;
    @BindView(R.id.settings_last_birth_switch)
    Switch lastBirthSwitch;
    @BindView(R.id.settings_due_date_switch)
    Switch dueDateSwitch;

    SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        presenter = new SettingsPresenterImpl(this);
        presenter.setView(this);
        initToolbar();
    }

    @Override
    protected void onDestroy() {
        presenter.clearView();
        presenter = null;
        super.onDestroy();
    }

    @OnCheckedChanged({R.id.settings_weight_switch,
            R.id.settings_origin_switch,
            R.id.settings_limitations_switch,
            R.id.settings_entry_switch,
            R.id.settings_departure_switch,
            R.id.settings_castration_switch,
            R.id.settings_last_birth_switch,
            R.id.settings_due_date_switch})
    void onCheckedChanged(CompoundButton button, boolean checked) {
        switch (button.getId()) {
            case R.id.settings_weight_switch:
                presenter.onWeightChecked(checked);
                break;
            case R.id.settings_origin_switch:
                presenter.onOriginChecked(checked);
                break;
            case R.id.settings_limitations_switch:
                presenter.onLimitationsChecked(checked);
                break;
            case R.id.settings_entry_switch:
                presenter.onEntryChecked(checked);
                break;
            case R.id.settings_departure_switch:
                presenter.onDepartureChecked(checked);
                break;
            case R.id.settings_castration_switch:
                presenter.onCastrationChecked(checked);
                break;
            case R.id.settings_last_birth_switch:
                presenter.onLastBirthChecked(checked);
                break;
            case R.id.settings_due_date_switch:
                presenter.onDueDateChecked(checked);
                break;
        }
    }

    @Override
    public void setWeightChecked(boolean checked) {
        weightSwitch.setChecked(checked);
    }

    @Override
    public void setOriginChecked(boolean checked) {
        originSwitch.setChecked(checked);
    }

    @Override
    public void setLimitationsChecked(boolean checked) {
        limitationsSwitch.setChecked(checked);
    }

    @Override
    public void setEntryChecked(boolean checked) {
        entrySwitch.setChecked(checked);
    }

    @Override
    public void setDepartureChecked(boolean checked) {
        departureSwitch.setChecked(checked);
    }

    @Override
    public void setCastrationChecked(boolean checked) {
        castrationSwitch.setChecked(checked);
    }

    @Override
    public void setLastBirthChecked(boolean checked) {
        lastBirthSwitch.setChecked(checked);
    }

    @Override
    public void setDueDateChecked(boolean checked) {
        dueDateSwitch.setChecked(checked);
    }
}

import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import { Button, Checkbox, Table, Icon, Input } from 'semantic-ui-react';
import DateInput from 'components/shared/inputs/DateInput';

export default class PayoutRow extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: null,
            disabled: true
        };
    }

    editClickHandler = () => this.setState({ disabled: false });

    saveClickHandler = () => {
        this.setState({ disabled: true });
        this.props.updatePayment(this.state.data);
    };

    inputChangeHandler = (type, e, { value }) => {
        const field = {};
        switch (type) {
            case 'date_picker':
                field.date = moment(value).unix();
                break;
            case 'amount':
                field.amount = Number(value);
                break;
            default:
                field[type] = value;
                break;
        }
        this.setState({ data: { ...this.state.data, ...field } });
    };

    exgChangeHandler = (e, { checked }) => {
        this.setState({ data: { ...this.state.data, exg: checked } });
    };

    disableEdit = () => {
        this.setState({
            data: this.props.data,
            disabled: true
        });
    };

    removePayment = () => {
        const { removePayment, id, data } = this.props;
        removePayment(id, data.id);
    };

    componentDidMount() {
        this.setState({ data: this.props.data });
    }

    render() {
        const { disabled, data } = this.state;
        return data ? (
            <React.Fragment>
                <Table.Cell>
                    <Input
                        type="number"
                        value={data.amount}
                        disabled={disabled}
                        onChange={this.inputChangeHandler.bind(this, 'amount')}
                    />
                </Table.Cell>
                <Table.Cell>
                    <Input
                        type="text"
                        value={data.note}
                        disabled={disabled}
                        onChange={this.inputChangeHandler.bind(this, 'note')}
                    />
                </Table.Cell>
                <Table.Cell>
                    {disabled ? (
                        moment.unix(data.date).format('DD MM YYYY')
                    ) : (
                        <DateInput changeHandler={this.inputChangeHandler} />
                    )}
                </Table.Cell>
                <Table.Cell>
                    <Checkbox
                        checked={data.exg}
                        disabled={disabled}
                        onChange={this.exgChangeHandler}
                    />
                </Table.Cell>
                <Table.Cell textAlign="right">
                    {disabled ? (
                        <Button
                            onClick={this.editClickHandler}
                            icon
                            basic
                            color="black"
                        >
                            <Icon name="edit" />
                        </Button>
                    ) : (
                        <React.Fragment>
                            <Button
                                onClick={this.disableEdit}
                                icon
                                basic
                                color="grey"
                            >
                                <Icon name="close" />
                            </Button>
                            <Button
                                onClick={this.removePayment}
                                icon
                                basic
                                color="red"
                            >
                                <Icon name="trash outline" />
                            </Button>
                            <Button
                                onClick={this.saveClickHandler}
                                primary
                                icon
                            >
                                <Icon name="save" />
                            </Button>
                        </React.Fragment>
                    )}
                </Table.Cell>
            </React.Fragment>
        ) : null;
    }
}

PayoutRow.propTypes = {
    data: PropTypes.object.isRequired,
    updatePayment: PropTypes.func.isRequired,
    removePayment: PropTypes.func.isRequired,
    id: PropTypes.string.isRequired
};

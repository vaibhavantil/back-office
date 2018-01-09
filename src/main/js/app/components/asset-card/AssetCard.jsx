import React from 'react';
import { Card, Image, Dropdown, Button } from 'semantic-ui-react';
import moment from 'moment';
import { assetStates } from 'app/lib/selectOptions';

/* eslint-disable react/prop-types */
export default class AssetCard extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            disabled: false,
            dropdownValue: ''
        };
        this.dropdownHandler = this.dropdownHandler.bind(this);
        this.saveClickHandler = this.saveClickHandler.bind(this);
    }

    dropdownHandler(e, { value }) {
        this.setState(() => ({ dropdownValue: value }));
    }
    saveClickHandler() {
        this.setState(() => ({ disabled: true }));
        this.props.assetUpdate(this.props.asset.id, this.state.dropdownValue);
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.updateStatus !== this.props.updateStatus) {
            this.setState({
                disabled: false
            });
        }
    }

    render() {
        const { asset } = this.props;
        const assetDate = moment(
            asset.registrationDate,
            'YYYY-MM-DD HH:mm'
        ).format('MMMM Do YYYY');
        return (
            <Card>
                <Image src={asset.photoUrl} />
                <Card.Content>
                    <Card.Header>{asset.title}</Card.Header>
                    <Card.Meta>{assetDate}</Card.Meta>
                    <Card.Description>{asset.userId}</Card.Description>
                </Card.Content>
                <Card.Content extra>
                    <div>
                        <Dropdown
                            className="dropdown--short-text"
                            onChange={this.dropdownHandler}
                            options={assetStates}
                            placeholder="Choose asset state"
                            selection
                            style={{ width: '100%' }}
                            value={this.state.dropdownValue || asset.state}
                            disabled={this.state.disabled}
                        />
                        <Button primary fluid onClick={this.saveClickHandler}>
                            Save
                        </Button>
                    </div>
                </Card.Content>
            </Card>
        );
    }
}

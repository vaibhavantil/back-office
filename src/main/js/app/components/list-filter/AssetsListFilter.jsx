import React from 'react';
import { Button, Dropdown } from 'semantic-ui-react';
import { assetStates } from 'app/lib/selectOptions';

export default class Fliter extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            filters: []
        };
    }

    componentDidMount() {
        if (!assetStates[6]) {
            assetStates.push({
                key: 7,
                text: 'ALL',
                value: 'ALL'
            });
        }
        this.setState({
            filters: assetStates
        });
    }

    render() {
        const { filterChange, activeFilter, polling, pollingHandler } = this.props;
        return (
            <div className="filter">
                <label className="filter__label">Select asset state: </label>
                <Dropdown
                    onChange={filterChange}
                    options={this.state.filters}
                    selection
                    value={activeFilter}
                />
                <Button onClick={pollingHandler}>
                    Poll {polling ? 'stop' : 'start'}
                </Button>
            </div>
        );
    }
}
